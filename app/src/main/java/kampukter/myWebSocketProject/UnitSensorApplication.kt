package kampukter.myWebSocketProject

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

class UnitSensorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val appSharedPreferences = getSharedPreferences(APP_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        if (appSharedPreferences.contains(APP_PREFERENCES_IPADDR)) {
            appSharedPreferences.getStringSet(APP_PREFERENCES_IPADDR, null).let {
                ipAddressUnit = it.toTypedArray()
            }
        }
    }
    companion object {
        const val APP_PREFERENCES = "appSettings"
        const val APP_PREFERENCES_IPADDR = "usedIpAddresses"
        var ipAddressUnit = arrayOf("ws://109.254.66.131:81", "ws://192.168.0.82:81")
    }
}