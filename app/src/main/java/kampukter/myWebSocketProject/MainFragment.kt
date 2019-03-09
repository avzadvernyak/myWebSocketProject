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
import okhttp3.*
import java.util.concurrent.TimeUnit

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

        val client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("ws://192.168.0.82:81")
            .build()
        val wsListener = EchoWebSocketListener()

        // this provide to make 'Open ws connection'
        client.newWebSocket(request, wsListener).run { Log.d("blablabla", "Start") }

        temperature.observe(this, Observer { temp-> firstSensorValueTextView.text = temp })
        logProcessWS.observe(this, Observer { msg-> logTextView.text = msg })

        startButton.setOnClickListener {
            logTextView.text = "Press Start Button"
            Log.d("blablabla", "Press Start Button")
        }
        stopButton.setOnClickListener {
            logTextView.text = "Press Stop Button"
            Log.d("blablabla", "Press Stop Button")
        }
    }

}

private class EchoWebSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        logProcessWS.postValue( "Press Stop Button" )
        Log.v("blablabla", "Open WebSocket")
    }
    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        super.onFailure(webSocket, t, response)
        logProcessWS.postValue( """Failure t - ${t.toString()}""" )
        Log.v("blablabla", """Failure t - ${t.toString()}""")
        Log.v("blablabla", """Failure response - ${response.toString()}""")
    }
    override fun onMessage(webSocket: WebSocket?, text: String?) {
        super.onMessage(webSocket, text)
        Log.v("blablabla", "Receiving : $text")
        temperature.postValue(text)
        logProcessWS.postValue( "Receiving : $text" )
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        super.onClosing(webSocket, code, reason)
        Log.v("blablabla", "Closing : $code / $reason")

        logProcessWS.postValue( "Closing : $code / $reason" )
        webSocket!!.close(NORMAL_CLOSURE_STATUS, null)
    }

    companion object {
        private val NORMAL_CLOSURE_STATUS = 1000
    }
}