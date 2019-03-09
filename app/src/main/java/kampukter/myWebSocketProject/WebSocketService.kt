package kampukter.myWebSocketProject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketService : Service() {

    lateinit var client: OkHttpClient

    override fun onBind(intent: Intent): IBinder? {

        Log.d("blablabla", "onBind service")
        return null
    }

    override fun onCreate() {
        super.onCreate()

        client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("ws://192.168.0.82:81")
            .build()
        val wsListener = EchoWebSocketListener()

        // this provide to make 'Open ws connection'
        client.newWebSocket(request, wsListener).run { Log.d("blablabla", "Start") }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("blablabla", "Start service")
        if (flags and Service.START_FLAG_RETRY == 0) {
            // TODO Если это повторный запуск, выполнить какие-то действия.
        } else {
            // TODO Альтернативные действия в фоновом режиме.
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("blablabla", "Stop service")
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
}
