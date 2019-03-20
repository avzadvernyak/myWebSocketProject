package kampukter.myWebSocketProject.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kampukter.myWebSocketProject.Data.InfoSensor
import retrofit2.Call
import retrofit2.Callback

class InfoSensorRepository {

    private val apiInfoSensor = ApiInfoSensor.create()
    val result = MutableLiveData<List<InfoSensor>>()
    fun getInfoSensor() {
        val call = apiInfoSensor.getItems("ESP8266-01", "2019-03-20")
        call.enqueue(object : Callback<List<InfoSensor>> {
            override fun onResponse(call: Call<List<InfoSensor>>, response: retrofit2.Response<List<InfoSensor>>?) {
                response?.body().let { result.postValue(response?.body())}
            }
            override fun onFailure(call: Call<List<InfoSensor>>, t: Throwable) {
                Log.e("blablabla", t.toString())
            }
        })

    }
}