package kampukter.myWebSocketProject.UI

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kampukter.myWebSocketProject.Data.RequestPeriod
import kampukter.myWebSocketProject.Data.ResultInfoSensor
import kampukter.myWebSocketProject.R
import kampukter.myWebSocketProject.UI.DialogFragment.DatePickerFragment
import kampukter.myWebSocketProject.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.sensor1_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class Sensor1InformationFragment : Fragment() {

    private val mainViewModel by viewModel<MainViewModel>()
    private var infoSensor1Adapter: InfoSensor1Adapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.sensor1_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = "Sensor1 Info"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        infoSensor1Adapter = InfoSensor1Adapter()

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = infoSensor1Adapter
        }


        val format = SimpleDateFormat("yyyy-MM-dd")
        var currentDay = Date()
        val strDateEnd = format.format(currentDay)


        mainViewModel.getQuestionInfoSensor(RequestPeriod("ESP8266-01", strDateEnd, strDateEnd))
        progressBar.visibility = View.VISIBLE
        mainViewModel.infoSensor.observe(this, Observer { infoSensorList ->
            when (infoSensorList) {
                is ResultInfoSensor.Success -> {
                    progressBar.visibility = View.GONE
                    dateTextView.text = getString(R.string.dateInfoView, DateFormat.format("dd/MM/yyyy", currentDay))
                    val dateMax =
                        infoSensorList.infoSensor.maxBy { it.value }?.date?.let { time ->
                            DateFormat.format("HH:mm", time * 1000L)
                        }
                    maxTextView.text = getString(
                        R.string.maxValuePeriod,
                        infoSensorList.infoSensor.maxBy { it.value }?.value.toString(),
                        dateMax.toString()
                    )

                    val dateMin =
                        infoSensorList.infoSensor.minBy { it.value }?.date?.let { time ->
                            DateFormat.format("HH:mm", time * 1000L)
                        }
                    minTextView.text = getString(
                        R.string.minValuePeriod,
                        infoSensorList.infoSensor.minBy { it.value }?.value.toString(),
                        dateMin.toString()
                    )
                    val sumValue = infoSensorList.infoSensor.sumBy { it.value.toInt() }
                    averageTextView.text = getString(
                        R.string.averageValuePeriod,
                        (sumValue / infoSensorList.infoSensor.size).toString()
                    )
                    infoSensor1Adapter?.setList(infoSensorList.infoSensor)
                }
                is ResultInfoSensor.OtherError -> {
                    progressBar.visibility = View.GONE
                    Log.d("blablabla", "Other Error" + infoSensorList.tError)
                }
                is ResultInfoSensor.EmptyResponse -> {
                    progressBar.visibility = View.GONE
                    Snackbar.make(
                        sensor1_fragment,
                        getString(R.string.noDataMessage, DateFormat.format("dd/MM/yyyy", currentDay)),
                        Snackbar.LENGTH_LONG
                    ).show()

                    Log.d("blablabla", "infoSensorList is Empty")
                }
            }
        })
        calendarFAB.setOnClickListener {


            val c = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _,
                                                                       year, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, monthOfYear)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                currentDay = c.time
                val searchDate = format.format(c.time)
                mainViewModel.getQuestionInfoSensor(RequestPeriod("ESP8266-01", searchDate, searchDate))
                progressBar.visibility = View.VISIBLE
            }

            fragmentManager?.let {
                DatePickerFragment.create(dateSetListener)
                    .show(it, "datePicker")
            }
        }
    }
}