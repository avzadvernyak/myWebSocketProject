package kampukter.myWebSocketProject.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kampukter.myWebSocketProject.Data.RequestLocation
import kampukter.myWebSocketProject.Data.RequestPeriod
import kampukter.myWebSocketProject.Data.ResultInfoSensor
import kampukter.myWebSocketProject.Data.ResultLocation
import kampukter.myWebSocketProject.repository.InfoSensorRepository
import kampukter.myWebSocketProject.repository.LocationRepository
import kampukter.myWebSocketProject.repository.WebSocketRepository

class MainViewModel(
    private val webSocketRepository: WebSocketRepository,
    private val infoSensorRepository: InfoSensorRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    val locationAddr = locationRepository.result

    val _location = MutableLiveData<RequestLocation>()
    val locAdr: LiveData<ResultLocation> = Transformations.switchMap(_location) { question ->
        locationRepository.getFetchAddress(question)
    }
    fun getLocation(arg:RequestLocation){
        _location.postValue(arg)
    }



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