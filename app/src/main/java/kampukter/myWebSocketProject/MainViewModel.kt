package kampukter.myWebSocketProject

import androidx.lifecycle.ViewModel

class MainViewModel(private val webSocketRepository: WebSocketRepository) : ViewModel() {

    val isConnect = webSocketRepository.getIsConnect()
    val logProcess = webSocketRepository.getLogProcess()
    val sensorInfo = webSocketRepository.getSensorInfo()
    val unitValue = webSocketRepository.getUnitValue()
    val infoIpAddressUnit = webSocketRepository.getInfoIpAddressUnit()

    fun setStatusAutomatic(isChecked: Boolean) = webSocketRepository.setStatusAutomatic(isChecked)
    fun setStatusRelay(isChecked: Boolean) = webSocketRepository.setStatusRelay(isChecked)
    fun sendConnectionControlEvent() = webSocketRepository.sendConnectionControlEvent()

}