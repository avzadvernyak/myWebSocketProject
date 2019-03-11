package kampukter.myWebSocketProject

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketService : Service() {

    private var ws: WebSocket? = null
    lateinit var client: OkHttpClient
    private val binder = MyBinder()

    override fun onBind(intent: Intent): IBinder? {

        Log.d("blablabla", "onBind service")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("blablabla", "------------------- onCreate service")
        myWebSocket()
    }

    internal inner class MyBinder : Binder() {

        // размести вот здесь

        fun getService(): WebSocketService {
            return this@WebSocketService
        }
    }

    fun setRelay1(isCheck: Boolean) {
        if (isCheck) {
            ws?.send("Relay1On")
            Log.v("blablabla", "Send Relay1On")
        } else {
            ws?.send("Relay1Off")
            Log.v("blablabla", "Send Relay1Off")
        }
    }

    //wan IP ws://109.254.66.131:81
    //lan IP ws://192.168.0.82:81
    private fun myWebSocket() {
        client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("ws://109.254.66.131:81")
            .build()
        val wsListener = EchoWebSocketListener()

        client.newWebSocket(request, wsListener).run {
            ws = this
            Log.d("blablabla", "Start")
        }

    }

    private class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            logProcessWS.postValue("Open WebSocket")
        }

        override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
            super.onFailure(webSocket, t, response)
            logProcessWS.postValue("""Connect Error - ${t.toString()}""")
            Log.v("blablabla", """Connect WebSocket Error - ${t.toString()}""")
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            super.onMessage(webSocket, text)
            //Log.v("blablabla", "Receiving : $text")
            if (text == "Connected") logProcessWS.postValue("Receiving : $text")
            else {
                val result = Gson().fromJson(text, UnitSensorInfo::class.java)
                sensorInfo.postValue(result)
                logProcessWS.postValue("Receiving from ${result.unit}")
            }
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            super.onClosing(webSocket, code, reason)
            //Log.v("blablabla", "Closing : $code / $reason")
            logProcessWS.postValue("Closing WebSocket session : $code")
            //webSocket!!.close(NORMAL_CLOSURE_STATUS, null)
        }
    }
    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
