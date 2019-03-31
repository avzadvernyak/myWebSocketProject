package kampukter.myWebSocketProject.UI

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*
import android.app.DatePickerDialog.OnDateSetListener


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var onDateSetListener: OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), onDateSetListener, year, month, day)
    }

    private fun setOnDateSetListener(listener: OnDateSetListener?) {
        this.onDateSetListener = listener
    }

    companion object {
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        fun create(onDateSetListener: DatePickerDialog.OnDateSetListener): DatePickerFragment =
            DatePickerFragment().apply {
                setOnDateSetListener(onDateSetListener)
            }

    }
}