package kampukter.myWebSocketProject.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import kampukter.myWebSocketProject.Constants
import kampukter.myWebSocketProject.Data.RequestLocation
import kampukter.myWebSocketProject.Data.ResultLocation
import kampukter.myWebSocketProject.FetchAddressIntentService
import kampukter.myWebSocketProject.R
import kampukter.myWebSocketProject.viewModel.MainViewModel
import kotlinx.android.synthetic.main.location_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

private const val REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY"

class LocationActivity : AppCompatActivity() {

    private val mainViewModel by viewModel<MainViewModel>()

    private var requestingLocationUpdates: Boolean = true
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val location = MutableLiveData<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)

        savedInstanceState?.let {
            requestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        location.observe(this, Observer { _location ->
            locationLatitudeTextView.text = _location.latitude.toString()
            locationLongitudeTextView.text = _location.longitude.toString()
            altitudeTextView.text = _location.altitude.toString()
            dateTextView.text = convertLongToTime(_location.time)
            startServiceButton1.setOnClickListener {
                mainViewModel.getLocation(RequestLocation(_location, this))
            }
        })
        mainViewModel.locationAddr.observe(this, Observer { resultLocation ->
            when (resultLocation) {
                is ResultLocation.Success -> addressTextView.text = resultLocation.location
                is ResultLocation.EmptyResponse -> addressTextView.text = "Not found"
                is ResultLocation.OtherError -> addressTextView.text = resultLocation.tError
            }
        })

        mainViewModel.locAdr.observe(this, Observer { resultLocation ->
            when (resultLocation) {
                is ResultLocation.Success -> addressTextView1.text = resultLocation.location
                is ResultLocation.EmptyResponse -> addressTextView1.text = "Not found"
                is ResultLocation.OtherError -> addressTextView1.text = resultLocation.tError
            }
        })
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                Log.d("blablabla", locationResult.locations.size.toString())
                for (loc in locationResult.locations) {
                    location.postValue(loc)

                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                Log.d("blablabla", "LocationAvailability")
            }
        }
        startServiceButton.setOnClickListener { startIntentService() }

    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest().apply {
            interval = 60 * 1000
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

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm:ss dd.MM.yyyy ")
        return format.format(date)
    }

    private fun startIntentService() {
        val intent = Intent(this, FetchAddressIntentService::class.java).apply {
            putExtra(Constants.LOCATION_DATA_EXTRA, location.value)
        }
        startService(intent)
    }
}