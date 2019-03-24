package kampukter.myWebSocketProject.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kampukter.myWebSocketProject.Data.InfoSensor
import kampukter.myWebSocketProject.Data.RequestPeriod
import kampukter.myWebSocketProject.Data.ResultInfoSensor
import retrofit2.Call
import retrofit2.Callback

class InfoSensorRepository {

    private val apiInfoSensor = ApiInfoSensor.create()
    val result = MutableLiveData<ResultInfoSensor>()
    fun getDataPeriod(request: RequestPeriod): LiveData<ResultInfoSensor> {
        val call = apiInfoSensor.getInfoSensorPeriod(request.unit, request.dateBegin, request.dateEnd)
        call.enqueue(object : Callback<List<InfoSensor>> {
            override fun onResponse(call: Call<List<InfoSensor>>, response: retrofit2.Response<List<InfoSensor>>) {
                //response?.body().let { result.postValue(response?.body()) }
                if (response.isSuccessful) response.body()?.let { result.postValue(ResultInfoSensor.Success(it)) }
                else result.postValue(ResultInfoSensor.EmptyResponse)
            }

            override fun onFailure(call: Call<List<InfoSensor>>, t: Throwable) {
                Log.e("blablabla", t.toString())
                result.postValue(ResultInfoSensor.OtherError(t.toString()))
            }
        })
        return result
    }
}