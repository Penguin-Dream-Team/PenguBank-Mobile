package club.pengubank.mobile.utils.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.startActivityForResult
import java.util.*


class Bluetooth  {

    //var listen: Button? = null, var send:Button? = null, var listDevices:Button? = null
    //var listView: ListView? = null
    //var msg_box: TextView? = null, var status:TextView? = null
    //var writeMsg: EditText? = null

    var bluetoothAdapter: BluetoothAdapter? = null
    var btArray = ByteArray(1024)

    var sendReceive: SendReceive? = null

    val STATE_LISTENING = 1
    val STATE_CONNECTING = 2
    val STATE_CONNECTED = 3
    val STATE_CONNECTION_FAILED = 4
    val STATE_MESSAGE_RECEIVED = 5

    var REQUEST_ENABLE_BLUETOOTH = 1

    val APP_NAME = "BTChat"
    val MY_UUID: UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66")

    fun create(savedInstanceState: Bundle?) {
        //super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        //findViewByIdes()
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(Activity(), enableIntent, REQUEST_ENABLE_BLUETOOTH, null)
        }
        //implementListeners()
    }

    /*private fun implementListeners() {
        listDevices.setOnClickListener(object : OnClickListener() {
            fun onClick(view: View?) {
                val bt = bluetoothAdapter!!.bondedDevices
                val strings = arrayOfNulls<String>(bt.size)
                btArray = arrayOfNulls(bt.size)
                var index = 0
                if (bt.size > 0) {
                    for (device in bt) {
                        btArray[index] = device
                        strings[index] = device.name
                        index++
                    }
                    val arrayAdapter =
                        ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_1, strings)
                    listView.setAdapter(arrayAdapter)
                }
            }
        })
        listen.setOnClickListener(object : OnClickListener() {
            fun onClick(view: View?) {
                val serverClass = ServerClass()
                serverClass.start()
            }
        })
        listView.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
            val clientClass = ClientClass(btArray[i])
            clientClass.start()
            status.setText("Connecting")
        })
        send.setOnClickListener(object : OnClickListener() {
            fun onClick(view: View?) {
                val string = writeMsg!!.text.toString()
                sendReceive!!.write(string.toByteArray())
            }
        })
    }

    var handler: Handler = Handler(object : Callback() {
        fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                STATE_LISTENING -> status.setText("Listening")
                STATE_CONNECTING -> status.setText("Connecting")
                STATE_CONNECTED -> status.setText("Connected")
                STATE_CONNECTION_FAILED -> status.setText("Connection Failed")
                STATE_MESSAGE_RECEIVED -> {
                    val readBuff = msg.obj as ByteArray
                    val tempMsg = String(readBuff, 0, msg.arg1)
                    msg_box!!.text = tempMsg
                }
            }
            return true
        }
    })

    private fun findViewByIdes() {
        listen = findViewById(R.id.listen) as Button?
        send = findViewById(R.id.send) as Button
        listView = findViewById(R.id.listview) as ListView?
        msg_box = findViewById(R.id.msg) as TextView?
        status = findViewById(R.id.status) as TextView
        writeMsg = findViewById(R.id.writemsg) as EditText?
        listDevices = findViewById(R.id.listDevices) as Button
    }*/
}