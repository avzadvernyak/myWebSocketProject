package kampukter.myWebSocketProject.UI

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kampukter.myWebSocketProject.R
import kampukter.myWebSocketProject.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.sensor1_full_screen_graph_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class Sensor1FullScreenGraph : AppCompatActivity() {

    private val mainViewModel by viewModel<MainViewModel>()
    lateinit var series: LineGraphSeries<DataPoint>
    var lenArray = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.sensor1_full_screen_graph_activity)
        setSupportActionBar(sensor1FullScreenToolbar).apply { title = getString(R.string.titleGraphActivity) }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        Log.d("blablabla", "START Sensor1FullScreenGraph")

        mainViewModel.sensorInfo.observe(this, Observer { value ->
            series.appendData(DataPoint(lenArray++,value.sensor1.toDouble()),true,100)

        })
        series = LineGraphSeries()
        graphSensor1FS.addSeries(series)

        graphSensor1FS.title = "Sensor #1"


        // set manual X bounds
        graphSensor1FS.viewport.isXAxisBoundsManual = true
        graphSensor1FS.viewport.setMinX(0.0)
        graphSensor1FS.viewport.setMaxX(100.0)
        //graphSensor1FS.gridLabelRenderer.setHumanRounding(false)
        graphSensor1FS.gridLabelRenderer.isHorizontalLabelsVisible = false
        // set manual Y bounds
        graphSensor1FS.viewport.isYAxisBoundsManual = true
        graphSensor1FS.viewport.setMinY(-25.0)
        graphSensor1FS.viewport.setMaxY(40.0)
        graphSensor1FS.gridLabelRenderer.verticalAxisTitle = getString(R.string.titleTemperature)


    }
}