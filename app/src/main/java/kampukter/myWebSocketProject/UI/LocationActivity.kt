package kampukter.myWebSocketProject.UI

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kampukter.myWebSocketProject.R
import kotlinx.android.synthetic.main.location_activity.*
import java.util.*

private const val REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY"

class LocationActivity : AppCompatActivity() {

    private var requestingLocationUpdates: Boolean = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)

        savedInstanceState?.let {
            requestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
               /*
                locationResult ?: return
                for (location in locationResult.locations) {
                    //пока не надо
                }
                */
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
        fusedLocationClient.let { loc ->
            loc.lastLocation.addOnSuccessListener { location: Location? ->
                locationLatitudeTextView.text = location?.latitude.toString()
                locationLongitudeTextView.text = location?.longitude.toString()
                dateTextView.text = location?.time?.let{Date(it).toString()}
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            else Log.d("blablabla", "PERMISSION!!!!!!")

        } else
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

}