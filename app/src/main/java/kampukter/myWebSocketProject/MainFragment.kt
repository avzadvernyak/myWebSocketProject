package kampukter.myWebSocketProject

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

val sensorInfo = MutableLiveData<UnitSensorInfo>()
val logProcessWS = MutableLiveData<String>()
val isConnectToWebSocket = MutableLiveData<Boolean>()

class MainFragment : Fragment() {

    var myService: WebSocketService? = null
    private var wsConnect: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (wsConnect == null) {
            wsConnect = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                    // вот тут обсервить

                    val binder = service as WebSocketService.MyBinder
                    myService = binder.getService()
                }
                override fun onServiceDisconnected(name: ComponentName?) {
                }
            }
            activity?.bindService(Intent(activity, WebSocketService::class.java), wsConnect!!, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbarUnitState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Unit Info"
        }

        sensorInfo.observe(this, Observer {
            firstSensorValueTextView.text = it.sensor1.toString()
            statusRelayCheckBox.isChecked = (it.relay1)
        })
        logProcessWS.observe(this, Observer { msg -> logTextView.text = msg })
        isConnectToWebSocket.observe(this, Observer { isConnect ->
            if (isConnect) reconnectButton.visibility = View.GONE
            else reconnectButton.visibility = View.VISIBLE
        })

        reconnectButton.setOnClickListener {
            //logTextView.text = "Press Start Button"
            Log.d("blablabla", "Reconnect")
            myService?.myWebSocket()
            //activity?.bindService(Intent(activity, WebSocketService::class.java), wsConnect, Context.BIND_AUTO_CREATE)

        }
        /*
        stopButton.setOnClickListener {
            logTextView.text = "Press Stop Button"
            activity?.unbindService(wsConnect)
        }
        */
        statusRelayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            Log.d("blablabla", "myService.setRelay1")
            myService?.setRelay1(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("blablabla", "----------------onDestroy Fragment")
        //activity?.unbindService(wsConnect)

    }

}
