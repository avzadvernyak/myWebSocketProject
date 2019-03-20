package kampukter.myWebSocketProject.Repository

import kampukter.myWebSocketProject.Data.InfoSensor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInfoSensor {
    @GET("get_info.php?")
    fun getItems(@Query("sensor") unit: String, searchDate: String): Call<List<InfoSensor>>


    companion object Factory {
        val BASE_URL = "http://orbis.in.ua/api/"
        fun create(): ApiInfoSensor {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInfoSensor::class.java)
        }
    }
}