package kampukter.myWebSocketProject

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import kampukter.myWebSocketProject.Data.ResultLocation
import kampukter.myWebSocketProject.repository.LocationRepository
import org.koin.android.ext.android.inject
import java.io.IOException
import java.util.*

class FetchAddressIntentService : IntentService("FetchAddress") {

    private val locationRepository by inject<LocationRepository>()

    override fun onHandleIntent(intent: Intent) {
        val location =  intent.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)

        if (location == null) return

        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        } catch (ioException: IOException) {
            locationRepository.result.postValue(ResultLocation.OtherError("catch - IOException"))
            Log.d("blablabla", "catch - IOException")
        } catch (illegalArgumentException: IllegalArgumentException) {
            locationRepository.result.postValue(ResultLocation.OtherError("catch - IllegalArgumentException"))
            Log.d("blablabla", "catch - IllegalArgumentException")
        }
        if (addresses.isEmpty()) {
            Log.d("blablabla", "Addresses isEmpty()")
            locationRepository.result.postValue(ResultLocation.EmptyResponse)
        } else {
            val address = addresses[0]
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            Log.d("blablabla", addressFragments.joinToString(separator = "\n"))
            locationRepository.result.postValue(ResultLocation.Success(addressFragments.joinToString(separator = "\n")))
        }
    }
}