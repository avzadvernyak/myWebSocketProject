package kampukter.myWebSocketProject.UI

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kampukter.myWebSocketProject.Data.RequestPeriod
import kampukter.myWebSocketProject.Data.ResultInfoSensor
import kampukter.myWebSocketProject.R
import kampukter.myWebSocketProject.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.sensor1_full_screen_graph_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class Sensor1FullScreenGraph : AppCompatActivity() {

    private val mainViewModel by viewModel<MainViewModel>()
    lateinit var series: LineGraphSeries<DataPoint>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sensor1_full_screen_graph_activity)
        setSupportActionBar(sensor1FullScreenToolbar).apply { title = getString(R.string.titleGraphActivity) }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        Log.d("blablabla", "START Sensor1FullScreenGraph")
        val format = SimpleDateFormat("yyyy-MM-dd")
        val currentDay = Date()
        val strDateBegin = format.format(Date(currentDay.time - (1000 * 60 * 60 * 24)))
        val strDateEnd = format.format(currentDay)
        //val strDateBegin =strDateEnd
        mainViewModel.getQuestionInfoSensor(RequestPeriod("ESP8266-01", strDateBegin, strDateEnd))
        mainViewModel.infoSensor.observe(this, Observer { infoSensorList ->
            when (infoSensorList) {
                is ResultInfoSensor.Success -> {
                    val value = infoSensorList.infoSensor
                    val graphValue = Array(value.size) { i ->
                        DataPoint(Date(value[i].date * 1000L), value[i].value.toDouble())
                    }
                    series.resetData(graphValue)

                    graphSensor1FS.viewport.setMinX(value.first().date * 1000L.toDouble())
                    graphSensor1FS.viewport.setMaxX(value.last().date * 1000L.toDouble())
                    //graphSensor1FS.gridLabelRenderer.numHorizontalLabels = 6
                    //graphSensor1FS.gridLabelRenderer.isHorizontalLabelsVisible = true
                    titleTextView.text =
                        Date(value.first().date * 1000L).toString() + "\n" + Date(value.last().date * 1000L).toString()
                }
                is ResultInfoSensor.OtherError -> {
                    Log.d("blablabla", "Other Error" + infoSensorList.tError)
                    finish()
                }
                is ResultInfoSensor.EmptyResponse -> {
                    Log.d("blablabla", "infoSensorList is Empty")
                    finish()
                }

            }

        })
        series = LineGraphSeries()
        graphSensor1FS.addSeries(series)
        graphSensor1FS.title = "Sensor #1"
        //graphSensor1FS.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this, SimpleDateFormat("HH:mm"))
        graphSensor1FS.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this)
        graphSensor1FS.gridLabelRenderer.isHorizontalLabelsVisible = false
        graphSensor1FS.viewport.isXAxisBoundsManual = true
        graphSensor1FS.viewport.isYAxisBoundsManual = false
        graphSensor1FS.viewport.setMinY(0.0)
        graphSensor1FS.viewport.setMaxY(25.0)
        graphSensor1FS.gridLabelRenderer.verticalAxisTitle = getString(R.string.titleTemperature)
    }
}