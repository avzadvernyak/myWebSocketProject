package kampukter.myWebSocketProject

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.IBinder
import android.view.*
import androidx.appcompat.app.AppCompatActivity

class MainFragment : Fragment() {

    var myService: WebSocketService? = null
    private var wsConnect: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (wsConnect == null) {
            wsConnect = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                    val binder = service as WebSocketService.MyBinder
                    myService = binder.getService()
                    binder.getStateConnect().observe(this@MainFragment, Observer { isConnect ->
                        if (isConnect) reconnectButton.visibility = View.GONE
                        else reconnectButton.visibility = View.VISIBLE
                    })
                    binder.getSensorInfo().observe(this@MainFragment, Observer {
                        firstSensorValueTextView.text = it.sensor1.toString()
                        statusRelayCheckBox.setOnCheckedChangeListener(null)
                        statusAutomaticCheckBox.setOnCheckedChangeListener(null)
                        statusRelayCheckBox.isChecked = (it.relay1)
                        statusAutomaticCheckBox.isChecked = (it.relay2)
                        statusRelayCheckBox.setOnCheckedChangeListener { _, isChecked ->
                            myService?.setRelay1(isChecked)
                        }
                        statusAutomaticCheckBox.setOnCheckedChangeListener { _, isChecked ->
                            myService?.setRelay2(isChecked)
                        }
                    })
                    binder.getLogProcessWS()
                        .observe(this@MainFragment, Observer { msg -> logTextView.text = msg })
                    binder.getInfoUnitConnect()
                        .observe(this@MainFragment, Observer { inf -> infoUnitTextView.text = inf })
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbarUnitState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Unit Info"
        }
        reconnectButton.setOnClickListener {
            myService?.myWebSocket()
        }
        statusRelayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            myService?.setRelay1(isChecked)
        }
        statusAutomaticCheckBox.setOnCheckedChangeListener { _, isChecked ->
            myService?.setRelay2(isChecked)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
                )
    }

}
