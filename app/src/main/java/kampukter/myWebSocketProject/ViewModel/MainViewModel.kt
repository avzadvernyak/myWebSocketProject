package kampukter.myWebSocketProject.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kampukter.myWebSocketProject.Data.InfoSensor
import kampukter.myWebSocketProject.Data.RequestPeriod
import kampukter.myWebSocketProject.Data.ResultInfoSensor
import kampukter.myWebSocketProject.Repository.InfoSensorRepository
import kampukter.myWebSocketProject.Repository.WebSocketRepository

class MainViewModel(
    private val webSocketRepository: WebSocketRepository,
    private val infoSensorRepository: InfoSensorRepository
) : ViewModel() {

    val isConnect = webSocketRepository.getIsConnect()
    val logProcess = webSocketRepository.getLogProcess()
    val sensorInfo = webSocketRepository.getSensorInfo()
    val unitValue = webSocketRepository.getUnitValue()
    val infoIpAddressUnit = webSocketRepository.getInfoIpAddressUnit()

    private val searchData = MutableLiveData<RequestPeriod>()
    val infoSensor: LiveData<ResultInfoSensor> = Transformations.switchMap(searchData) { question ->
        infoSensorRepository.getDataPeriod(question)
    }
    fun getQuestionInfoSensor( setPeriod: RequestPeriod){
        searchData.postValue(setPeriod)
    }

    fun setStatusAutomatic(isChecked: Boolean) = webSocketRepository.setStatusAutomatic(isChecked)
    fun setStatusRelay(isChecked: Boolean) = webSocketRepository.setStatusRelay(isChecked)
    fun sendConnectionControlEvent() = webSocketRepository.sendConnectionControlEvent()

}