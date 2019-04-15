package kampukter.myWebSocketProject.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kampukter.myWebSocketProject.Data.RequestLocation
import kampukter.myWebSocketProject.Data.ResultLocation
import java.io.IOException
import java.util.*


class LocationRepository {
    val result = MutableLiveData<ResultLocation>()
    
    fun getFetchAddress(arg: RequestLocation): LiveData<ResultLocation> {
        val location = arg.location
        val context = arg.context
        val res = MutableLiveData<ResultLocation>()
        Thread(Runnable {
            val gc = Geocoder( context, Locale.getDefault())
            var addresses: List<Address> = emptyList()

            try {
                addresses = gc.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
            } catch (ioException: IOException) {
                res.postValue(ResultLocation.OtherError("catch - IOException"))
            } catch (illegalArgumentException: IllegalArgumentException) {
                res.postValue(ResultLocation.OtherError("catch - IllegalArgumentException"))
            }
            if (addresses.isEmpty()) {
                res.postValue(ResultLocation.EmptyResponse)
            } else {
                val address = addresses[0]
                val addressFragments = with(address) {
                    (0..maxAddressLineIndex).map { getAddressLine(it) }
                }
                res.postValue(ResultLocation.Success(addressFragments.joinToString(separator = "\n")))
            }
        }).start()
        return res
    }

}