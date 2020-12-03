package club.pengubank.mobile.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import club.pengubank.mobile.R
import club.pengubank.mobile.services.LoginService
import club.pengubank.mobile.states.LoginScreenState
import club.pengubank.mobile.states.StoreState
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun topBar() = TopAppBar(title = { Text("PenguBank") }, actions = {})

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var loginService: LoginService

    @Inject
    lateinit var store: StoreState

    private var previewView: PreviewView? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null

    private val screenAspectRatio: Int
        get() {
            // Get screen metrics used to setup camera for full screen resolution
            val metrics = DisplayMetrics().also { previewView?.display?.getRealMetrics(it) }
            return aspectRatio(metrics.widthPixels, metrics.heightPixels)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(topBar = { topBar() }) {
                MaterialTheme {
                    LoginScreen(loginService, store)
                }
            }
            SimpleCameraPreview()
        }

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

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val PERMISSION_CAMERA_REQUEST = 1

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }


    @Composable
    fun SimpleCameraPreview() {
        val lifecycleOwner = LifecycleOwnerAmbient.current
        val context = ContextAmbient.current
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        AndroidView({
            LayoutInflater.from(it).inflate(club.pengubank.mobile.R.layout.camera_host, null)
        }) { inflatedLayout ->
            //You can call
            // findViewById<>() and etc ... on inflatedLayout
            // here PreviewView is the root of my layout so I just cast it to
            // the PreviewView and no findViewById is required
            cameraProviderFuture.addListener(Runnable {
                val cameraProvider = cameraProviderFuture.get()
                bindAnalysis(
                    lifecycleOwner,
                    inflatedLayout as PreviewView /*the inflated layout*/,
                    cameraProvider
                )


            }, ContextCompat.getMainExecutor(context))
        }
    }

    fun bindAnalysis(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        cameraProvider: ProcessCameraProvider
    ) {
        var preview: Preview = Preview.Builder().build()

        var cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(previewView.createSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }

        analysisUseCase = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(previewView!!.display.rotation)
            .build()
        val cameraExecutor = Executors.newSingleThreadExecutor()

        analysisUseCase?.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
            processImageProxy(barcodeScanner, imageProxy)
        })

        try {
            cameraProvider!!.bindToLifecycle(/* lifecycleOwner= */this,
                cameraSelector!!,
                analysisUseCase
            )
        } catch (illegalStateException: IllegalStateException) {
            illegalStateException.message?.let { Log.e(ContentValues.TAG, it) }
        } catch (illegalArgumentException: IllegalArgumentException) {
            illegalArgumentException.message?.let { Log.e(ContentValues.TAG, it) }
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach {
                    it.rawValue?.let { it1 -> Log.d("HERE------------------------", it1) }
                }
            }
            .addOnFailureListener {
                it.message?.let { it1 -> Log.e("NOT HERE-------------------------", it1) }
            }.addOnCompleteListener {
                // When the image is from CameraX analysis use case, must call image.close() on received
                // images when finished using them. Otherwise, new images may not be received or the camera
                // may stall.
                imageProxy.close()
            }
    }

}

@Composable
fun LoginForm(loginState: LoginScreenState, store: StoreState) {
    val uiState = loginState.state

    if (uiState == LoginScreenState.LoginUIState.Success) {
        Text(text = store.token)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = loginState.email, onValueChange = { loginState.email = it })
            TextField(value = loginState.password, onValueChange = { loginState.password = it })
            Button(
                onClick = { loginState.login() },
                enabled = uiState != LoginScreenState.LoginUIState.Loading
            ) {
                Text("Login")
            }
            if (uiState is LoginScreenState.LoginUIState.Error) {
                Text(uiState.message)
            }
        }
    }


}

@Composable
fun LoginScreen(loginService: LoginService, store: StoreState) {
    LoginForm(LoginScreenState(loginService), store)
}


fun bindPreview(
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    cameraProvider: ProcessCameraProvider
) {
    var preview: Preview = Preview.Builder().build()

    var cameraSelector: CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    preview.setSurfaceProvider(previewView.createSurfaceProvider())

    var camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
}



