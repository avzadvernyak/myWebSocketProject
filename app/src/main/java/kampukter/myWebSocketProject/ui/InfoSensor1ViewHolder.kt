package kampukter.myWebSocketProject.ui

import android.text.format.DateFormat
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kampukter.myWebSocketProject.Data.InfoSensor
import kotlinx.android.synthetic.main.info_sensor_item.view.*

class InfoSensor1ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(result: InfoSensor) {
        with(itemView) {
            dateTimeTextView.text = DateFormat.format("HH:mm", result.date * 1000L)
            //dateTimeTextView.text = result.date.toString()
            valueTextView.text = result.value.toString()
        }
    }
}
