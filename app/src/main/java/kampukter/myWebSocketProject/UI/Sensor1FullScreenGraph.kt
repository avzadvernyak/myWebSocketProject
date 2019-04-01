package kampukter.myWebSocketProject.UI

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
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
import android.view.*


class Sensor1FullScreenGraph : AppCompatActivity() {

    private val mainViewModel by viewModel<MainViewModel>()
    private lateinit var series: LineGraphSeries<DataPoint>

    private val format = SimpleDateFormat("yyyy-MM-dd")
    private var currentDay = Date()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sensor1_full_screen_graph_activity)
        setSupportActionBar(sensor1FullScreenToolbar).apply { title = getString(R.string.titleGraphActivity) }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        Log.d("blablabla", "START Sensor1FullScreenGraph")

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

                    val begTime = DateFormat.format(getString(R.string.formatDT), value.first().date * 1000L)
                    val endTime = DateFormat.format(getString(R.string.formatDT), value.last().date * 1000L)
                    titleTextView.text = getString(R.string.periodView, begTime.toString(), endTime.toString())

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
        graphSensor1FS.viewport.isYAxisBoundsManual = true
        graphSensor1FS.viewport.setMinY(0.0)
        graphSensor1FS.viewport.setMaxY(40.0)
        graphSensor1FS.gridLabelRenderer.verticalAxisTitle = getString(R.string.titleTemperature)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(kampukter.myWebSocketProject.R.menu.menu_graph_view_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_last_day -> {
                currentDay = Date()
                val strDateBegin = format.format(Date(currentDay.time - (1000 * 60 * 60 * 24)))
                val strDateEnd = format.format(currentDay)
                mainViewModel.getQuestionInfoSensor(RequestPeriod("ESP8266-01", strDateBegin, strDateEnd))
                return true
            }
            R.id.action_last_week -> {
                currentDay = Date()
                val strDateBegin = format.format(Date(currentDay.time - (1000 * 60 * 60 * 24 * 7)))
                val strDateEnd = format.format(currentDay)
                mainViewModel.getQuestionInfoSensor(RequestPeriod("ESP8266-01", strDateBegin, strDateEnd))
                return true
            }
            R.id.action_month -> {
                val c = Calendar.getInstance()
                val dateSetListener = DatePickerDialog.OnDateSetListener { _dp,
                                                                           year, monthOfYear, dayOfMonth ->
                    c.set(Calendar.YEAR, year)
                    c.set(Calendar.MONTH, monthOfYear)
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                }
                fragmentManager?.let {
                    MonthDatePickerFragment.create(dateSetListener)
                        .show(supportFragmentManager, "datePicker")
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}