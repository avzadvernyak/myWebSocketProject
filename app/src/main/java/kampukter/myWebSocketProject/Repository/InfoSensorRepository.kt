package kampukter.myWebSocketProject.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kampukter.myWebSocketProject.Data.InfoSensor
import kampukter.myWebSocketProject.Data.RequestPeriod
import retrofit2.Call
import retrofit2.Callback

class InfoSensorRepository {

    private val apiInfoSensor = ApiInfoSensor.create()
    val result = MutableLiveData<List<InfoSensor>>()
    fun getDataPeriod(request: RequestPeriod): MutableLiveData<List<InfoSensor>> {
        val call = apiInfoSensor.getInfoSensorPeriod(request.unit, request.dateBegin, request.dateEnd)
        call.enqueue(object : Callback<List<InfoSensor>> {
            override fun onResponse(call: Call<List<InfoSensor>>, response: retrofit2.Response<List<InfoSensor>>?) {
                response?.body().let { result.postValue(response?.body()) }
            }

            override fun onFailure(call: Call<List<InfoSensor>>, t: Throwable) {
                Log.e("blablabla", t.toString())
            }
        })
        return result
    }
}