package kampukter.myWebSocketProject

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kampukter.myWebSocketProject.UnitSensorApplication.Companion.ipAddressUnit
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit

class WebSocketService : Service() {

    private val isConnectToWebSocket = MutableLiveData<Boolean>()
    private val sensorInfo = MutableLiveData<UnitSensorInfo>()
    private val logProcessWS = MutableLiveData<String>()

    private var ws: WebSocket? = null
    private lateinit var client: OkHttpClient
    private val binder = MyBinder()
    private var selectedIP = 0

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }
    override fun onCreate() {
        super.onCreate()
        myWebSocket()
    }
    internal inner class MyBinder : Binder() {

        fun getService(): WebSocketService {
            return this@WebSocketService
        }
        fun getStateConnect(): LiveData<Boolean> {
            return isConnectToWebSocket
        }
        fun getSensorInfo(): LiveData<UnitSensorInfo> {
            return sensorInfo
        }
        fun getLogProcessWS(): LiveData<String> {
            return logProcessWS
        }
    }

    fun setRelay1(isCheck: Boolean) {
        if (isCheck) {
            ws?.send("Relay1On")
        } else {
            ws?.send("Relay1Off")
        }
    }

    fun myWebSocket() {
        isConnectToWebSocket.postValue(true)
        client = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(ipAddressUnit[selectedIP])
            .build()
        val wsListener = EchoWebSocketListener()
        logProcessWS.postValue("Connect to " + ipAddressUnit[selectedIP])
        client.newWebSocket(request, wsListener).run {
            ws = this
        }
        if (selectedIP == 0) selectedIP++
        else selectedIP = 0

    }

    inner class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            logProcessWS.postValue("Open WebSocket")
            isConnectToWebSocket.postValue(true)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            var errorMessage: String
            t.let { errorMessage = it.toString() }
            logProcessWS.postValue("""Connect Error - $errorMessage""")
            isConnectToWebSocket.postValue(false)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            if (text == "Connected") logProcessWS.postValue("Receiving : $text")
            else {
                val result = Gson().fromJson(text, UnitSensorInfo::class.java)
                sensorInfo.postValue(result)
                logProcessWS.postValue("Latest receipt from ${result.unit} in ${DateFormat.format("HH:mm:ss", Date())}")
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
        }
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
