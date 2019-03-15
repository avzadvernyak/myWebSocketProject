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
    private val infoIpAddressUnit = MutableLiveData<String>()
    private val unitValue = MutableLiveData<Deque<Float>>()

    private var ws: WebSocket? = null
    private val binder = MyBinder()


    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        myWebSocket()
    }

    internal inner class MyBinder : Binder() {

        fun getService(): WebSocketService = this@WebSocketService
        fun getStateConnect(): LiveData<Boolean> = isConnectToWebSocket
        fun getSensorInfo(): LiveData<UnitSensorInfo> = sensorInfo
        fun getLogProcessWS(): LiveData<String> = logProcessWS
        fun getInfoUnitConnect(): LiveData<String> = infoIpAddressUnit
        fun getUnitValue(): LiveData<Deque<Float>> = unitValue
    }

    fun setRelay1(isCheck: Boolean) {
        if (isCheck) {
            ws?.send("Relay1On")
        } else {
            ws?.send("Relay1Off")
        }
    }

    fun setRelay2(isCheck: Boolean) {
        if (isCheck) {
            ws?.send("AutomaticLightOn")
        } else {
            ws?.send("AutomaticLightOff")
        }
    }

    fun myWebSocket() {
        var selectedIP = 0
        isConnectToWebSocket.postValue(true)
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(ipAddressUnit[selectedIP])
            .build()
        val wsListener = EchoWebSocketListener()
        logProcessWS.postValue("Connect to " + ipAddressUnit[selectedIP])
        client.newWebSocket(request, wsListener).run {
            ws = this
            infoIpAddressUnit.postValue(ipAddressUnit[selectedIP])
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
                val myValue: Deque<Float> = LinkedList()
                myValue.addFirst(result.sensor1).run { if (myValue.size > 30) myValue.removeLast() }
                unitValue.postValue(myValue)

                sensorInfo.postValue(result)
                logProcessWS.postValue("Latest receipt from ${result.unit} in ${DateFormat.format("HH:mm:ss", Date())}")
            }
        }
        /*
        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
        }
        */
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}
