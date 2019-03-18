package kampukter.myWebSocketProject

import android.content.Intent
import android.os.IBinder
import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.google.gson.Gson
import kampukter.myWebSocketProject.UnitSensorApplication.Companion.ipAddressUnit
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit
import okhttp3.WebSocket
import org.koin.android.ext.android.inject


class WebSocketService : LifecycleService() {

    private val webSocketRepository by inject<WebSocketRepository>()

    private var ws: WebSocket? = null
    val myValue: Deque<Float> = LinkedList()
    private var selectedIP = 0

    override fun onCreate() {
        super.onCreate()

        webSocketRepository.connectionControlEvent.observe(this, Observer { myWebSocket() })
        webSocketRepository.isCheckedStatusRelay.observe(this, Observer { isCheck ->
            if (isCheck) ws?.send("Relay1On")
            else ws?.send("Relay1Off")
        })
        webSocketRepository.isCheckedStatusAutomatic.observe(
            this,
            Observer { isCheck ->
                if (isCheck) {
                    ws?.send("AutomaticLightOn")
                } else {
                    ws?.send("AutomaticLightOff")
                }
            })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        myWebSocket()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onDestroy() {
        Log.d("blablabla", "Destroy service")
        super.onDestroy()

    }

    private fun myWebSocket() {
        webSocketRepository.saveIsConnect(true)
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(ipAddressUnit[selectedIP])
            .build()
        val wsListener = EchoWebSocketListener()
        webSocketRepository.saveLogProcess("Connecting to " + ipAddressUnit[selectedIP])
        client.newWebSocket(request, wsListener).run {
            ws = this
            webSocketRepository.saveInfoIpAddressUnit(ipAddressUnit[selectedIP])
        }
        if (selectedIP == 0) selectedIP++
        else selectedIP = 0

    }

    inner class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)

            webSocketRepository.saveLogProcess("Opened WebSocket")
            webSocketRepository.saveIsConnect(true)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            var errorMessage: String
            t.let { errorMessage = it.toString() }
            webSocketRepository.saveLogProcess("""Connection Error - $errorMessage""")
            webSocketRepository.saveIsConnect(false)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("blablabla", "Receiving ...")
            if (text == "Connected") webSocketRepository.saveLogProcess("Receiving : $text")
            else {
                val result = Gson().fromJson(text, UnitSensorInfo::class.java)

                myValue.addLast(result.sensor1).run { if (myValue.size > 20) myValue.removeFirst() }
                webSocketRepository.saveUnitValue(myValue)

                webSocketRepository.saveSensorInfo(result)
                webSocketRepository.saveLogProcess(
                    "Latest receipt from ${result.unit} in ${DateFormat.format(
                        "HH:mm:ss",
                        Date()
                    )}"
                )
            }
        }

        override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
            webSocket?.close(1000, null)

            webSocketRepository.saveLogProcess("webSocket close")
            //Log.d("blablabla", "webSocket close")
        }
    }
}
