package kampukter.myWebSocketProject.UI

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kampukter.myWebSocketProject.Data.InfoSensor
import kampukter.myWebSocketProject.R

class InfoSensor1Adapter: RecyclerView.Adapter<InfoSensor1ViewHolder>() {

        private var infoSensor: List<InfoSensor>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoSensor1ViewHolder {
            return InfoSensor1ViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.info_sensor_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return infoSensor?.size ?: 0
        }

        override fun onBindViewHolder(holder: InfoSensor1ViewHolder, position: Int) {
            infoSensor?.get(position)?.let { item ->
                holder.bind(item)
            }
        }

        fun setList(list: List<InfoSensor>) {
            this.infoSensor = list
            Log.d("blablabla", "Size" + infoSensor!!.size.toString())
            notifyDataSetChanged()
        }
}