package kampukter.myWebSocketProject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.main_fragment.*
import android.content.Intent

val temperature  =  MutableLiveData<String>()
val logProcessWS =  MutableLiveData<String>()

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        temperature.observe(this, Observer { temp-> firstSensorValueTextView.text = temp })
        logProcessWS.observe(this, Observer { msg-> logTextView.text = msg })

        startButton.setOnClickListener {
            logTextView.text = "Press Start Button"
            Log.d("blablabla", "Press Start Button")
            activity?.startService(Intent(activity, WebSocketService::class.java))
        }
        stopButton.setOnClickListener {
            logTextView.text = "Press Stop Button"
            activity?.stopService(
                Intent(activity, WebSocketService::class.java)
            )
        }
    }

}
