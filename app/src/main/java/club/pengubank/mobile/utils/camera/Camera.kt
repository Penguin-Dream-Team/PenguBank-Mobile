package club.pengubank.mobile.utils.camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri.encode
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import club.pengubank.mobile.services.SetupService
import club.pengubank.mobile.states.StoreState
import club.pengubank.mobile.views.MainActivity
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Camera() {

    private var previewView: PreviewView? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null

    companion object {
        @KtorExperimentalAPI
        private val TAG = MainActivity::class.java.simpleName
        private const val PERMISSION_CAMERA_REQUEST = 1

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private val screenAspectRatio: Int
        get() {
            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { previewView?.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }

    /**
     *  [androidx.camera.core.ImageAnalysis],[androidx.camera.core.Preview] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    @SuppressLint("InflateParams", "WrongConstant")
    @Composable
    fun SimpleCameraPreview(
        navController: NavHostController,
        storeState: StoreState,
        setupService: SetupService
    ) {
        val lifecycleOwner = LifecycleOwnerAmbient.current
        val context = ContextAmbient.current
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

        AndroidView({
            LayoutInflater.from(it).inflate(club.pengubank.mobile.R.layout.camera_host, null)
        }) { inflatedLayout ->
            cameraProviderFuture.addListener(Runnable {
                val cameraProvider = cameraProviderFuture.get()
                bindAnalysis(
                    lifecycleOwner,
                    inflatedLayout as PreviewView,
                    cameraProvider,
                    navController,
                    storeState,
                    setupService
                )
            }, ContextCompat.getMainExecutor(context))
        }
    }

    private fun bindAnalysis(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        cameraProvider: ProcessCameraProvider,
        navController: NavHostController,
        storeState: StoreState,
        setupService: SetupService
    ) {
        val preview: Preview = Preview.Builder().build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.createSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(previewView.display.rotation)
            .build()
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
            processImageProxy(barcodeScanner, imageProxy, navController, storeState, setupService)
        })

        try {
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, analysisUseCase)
        } catch (illegalStateException: IllegalStateException) {
            illegalStateException.message?.let { Log.e(ContentValues.TAG, it) }
        } catch (illegalArgumentException: IllegalArgumentException) {
            illegalArgumentException.message?.let { Log.e(ContentValues.TAG, it) }
        }
    }

    @KtorExperimentalAPI
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
        navController: NavHostController,
        storeState: StoreState,
        setupService: SetupService
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener  { barcodes ->
                barcodes.forEach {
                    it.rawValue?.let {
                        it1 -> Log.d("HERE------------------------", it1)
                        imageProxy.close()

                        if (storeState.operation == "QRCode") {
                            storeState.qrcodeScanned = true
                            storeState.bluetoothMac = URLBuilder(it1).parameters[encode("bluetoothMac")].toString()
                            storeState.kPub = URLBuilder(it1).parameters[encode("kPub")].toString()
                        } else {
                            setupService.registerTOTP(URLBuilder(it1).parameters[encode("secret")].toString())
                        }

                        navController.backStack.clear()
                        navController.navigate("dashboard")
                    }
                }
            }
            .addOnFailureListener {
                it.message?.let { it1 -> Log.e("NOT HERE-------------------------", it1) }
            }.addOnCompleteListener {
                imageProxy.close()
            }
    }

    fun bindPreview(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        cameraProvider: ProcessCameraProvider
    ) {
        val preview: Preview = Preview.Builder().build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.createSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
    }
}