package kampukter.myWebSocketProject.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kampukter.myWebSocketProject.Data.UnitSensorInfo
import java.util.*

class WebSocketRepository {

    private val CONNECTION_CONTROL_EVENT = 1

    private val isConnectToWebSocket = MutableLiveData<Boolean>()
    fun getIsConnect(): LiveData<Boolean> = isConnectToWebSocket
    fun saveIsConnect(isConnect: Boolean) {
        isConnectToWebSocket.postValue(isConnect)
    }

    private val logProcess = MutableLiveData<String>()
    fun getLogProcess(): LiveData<String> = logProcess
    fun saveLogProcess(logProcessWS: String) {
        logProcess.postValue(logProcessWS)
    }

    private val sensorInfo = MutableLiveData<UnitSensorInfo>()
    fun getSensorInfo(): LiveData<UnitSensorInfo> = sensorInfo
    fun saveSensorInfo(sensorInfoWS: UnitSensorInfo) {
        sensorInfo.postValue(sensorInfoWS)
    }

    private val unitValue = MutableLiveData<Deque<Float>>()
    fun getUnitValue(): LiveData<Deque<Float>> = unitValue
    fun saveUnitValue(unitValueWS: Deque<Float>) {
        unitValue.postValue(unitValueWS)
    }

    private val infoIpAddressUnit = MutableLiveData<String>()
    fun getInfoIpAddressUnit(): LiveData<String> = infoIpAddressUnit
    fun saveInfoIpAddressUnit(infoIpAddressUnitWS: String) {
        infoIpAddressUnit.postValue(infoIpAddressUnitWS)
    }

    val isCheckedStatusAutomatic = MutableLiveData<Boolean>()
    fun setStatusAutomatic(isChecked: Boolean) {
        isCheckedStatusAutomatic.postValue(isChecked)
    }

    val isCheckedStatusRelay = MutableLiveData<Boolean>()
    fun setStatusRelay(isChecked: Boolean) {
        isCheckedStatusRelay.postValue(isChecked)
    }

    val connectionControlEvent = MutableLiveData<Int>()
    fun sendConnectionControlEvent(){
        connectionControlEvent.postValue(CONNECTION_CONTROL_EVENT)
    }
}
