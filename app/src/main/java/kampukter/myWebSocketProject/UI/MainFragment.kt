package kampukter.myWebSocketProject.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import android.content.Intent
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import kampukter.myWebSocketProject.R
import kampukter.myWebSocketProject.ViewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val KEY_SELECTED_ITEMS = "KEY_SELECTED_ITEMS"

class MainFragment : Fragment() {

    private val mainViewModel by viewModel<MainViewModel>()
    private var isShowTable = false

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
            mainViewModel.infoIpAddressUnit.observe(this@MainFragment, Observer { inf ->
                subtitle = inf
            })
        }
        savedInstanceState?.getBoolean(KEY_SELECTED_ITEMS)?.let { value ->
            isShowTable = value
        }

        if (isShowTable) {
            is_show_table_fab.show()
            is_show_graph_fab.hide()
        } else {
            is_show_graph_fab.show()
            is_show_table_fab.hide()
        }

        bottom_sensor_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_sensor1 -> {
                    if (!isShowTable) startActivity(Intent(activity, Sensor1FullScreenGraph::class.java))
                    else startActivity(Intent(activity, Sensor1Information::class.java))
                }
                R.id.action_sensor2 -> {
                }
                R.id.action_sensor3 -> {
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        mainViewModel.sensorInfo.observe(this, Observer {
            firstSensorValueTextView.text = it.sensor1.toString()
            statusRelayCheckBox.setOnCheckedChangeListener(null)
            statusAutomaticCheckBox.setOnCheckedChangeListener(null)
            statusRelayCheckBox.isChecked = (it.relay1)
            statusAutomaticCheckBox.isChecked = (it.relay2)
            statusRelayCheckBox.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setStatusRelay(isChecked)
            }
            statusAutomaticCheckBox.setOnCheckedChangeListener { _, isChecked ->
                mainViewModel.setStatusAutomatic(isChecked)
            }
        })
        mainViewModel.isConnect.observe(this, Observer { isConnect ->
            if (isConnect) {
                reconnectButton.visibility = View.GONE
                //graphSensor1.visibility = View.VISIBLE
            } else {
                reconnectButton.visibility = View.VISIBLE
                //graphSensor1.visibility = View.GONE
            }
        })
        mainViewModel.logProcess.observe(this, Observer { msg -> logTextView.text = msg })


        reconnectButton.setOnClickListener {
            mainViewModel.sendConnectionControlEvent()
        }
        is_show_table_fab.setOnClickListener {
            isShowTable = false
            is_show_table_fab.hide()
            is_show_graph_fab.show()
        }
        is_show_graph_fab.setOnClickListener {
            isShowTable = true
            is_show_graph_fab.hide()
            is_show_table_fab.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_SELECTED_ITEMS, isShowTable)
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
