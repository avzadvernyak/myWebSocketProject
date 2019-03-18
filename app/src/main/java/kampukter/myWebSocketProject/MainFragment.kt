package kampukter.myWebSocketProject

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import android.content.Intent
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private val mainViewModel by viewModel<MainViewModel>()

    var myService: WebSocketService? = null
    lateinit var series: LineGraphSeries<DataPoint>

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
        mainViewModel.unitValue.observe(this, Observer { uVal ->
            if (uVal.size > 0) {
                val value = uVal.toFloatArray()
                val graphValue = Array(20) { i ->
                    if (i - (19 - uVal.size) > 0) DataPoint(
                        i.toDouble(),
                        value[i - (20 - uVal.size)].toDouble()
                    )
                    else DataPoint(i.toDouble(), 0.0)
                }
                series.resetData(graphValue)
            }
        })
        mainViewModel.isConnect.observe(this, Observer { isConnect ->
            if (isConnect) reconnectButton.visibility = View.GONE
            else reconnectButton.visibility = View.VISIBLE
        })
        mainViewModel.logProcess.observe(this, Observer { msg -> logTextView.text = msg })
        mainViewModel.infoIpAddressUnit.observe(this, Observer { inf -> infoUnitTextView.text = inf })



        series = LineGraphSeries(Array(20) { DataPoint(0.0, 0.0) })
        graphSensor1.addSeries(series)
        graphSensor1.title = "Sensor #1"
        // set manual X bounds
        graphSensor1.viewport.isXAxisBoundsManual = true
        graphSensor1.viewport.setMinX(0.0)
        graphSensor1.viewport.setMaxX(20.0)
        graphSensor1.gridLabelRenderer.isHorizontalLabelsVisible = false
        // set manual Y bounds
        graphSensor1.viewport.isYAxisBoundsManual = true
        graphSensor1.viewport.setMinY(-25.0)
        graphSensor1.viewport.setMaxY(40.0)
        graphSensor1.gridLabelRenderer.verticalAxisTitle = getString(R.string.titleTemperature)



        reconnectButton.setOnClickListener {
            mainViewModel.sendConnectionControlEvent()
        }
        statusRelayCheckBox.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.setStatusRelay(isChecked)
        }
        statusAutomaticCheckBox.setOnCheckedChangeListener { _, isChecked ->
            mainViewModel.setStatusAutomatic(isChecked)
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
