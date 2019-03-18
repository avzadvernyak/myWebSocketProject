package kampukter.myWebSocketProject

import android.app.Application
import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UnitSensorApplication : Application() {

    private lateinit var mRunnable: Runnable
    private lateinit var mHandler: Handler

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@UnitSensorApplication)
            modules(APPLICATION_MODULE)
        }

        val appSharedPreferences = getSharedPreferences(APP_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        if (appSharedPreferences.contains(APP_PREFERENCES_IPADDR)) {
            appSharedPreferences.getStringSet(APP_PREFERENCES_IPADDR, null)?.let {
                ipAddressUnit = it.toTypedArray()
            }
        }
        mHandler = Handler()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {

            private val serviceIntent = Intent(this@UnitSensorApplication, WebSocketService::class.java)

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onForeground() {
                startService(serviceIntent)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onBackground() {
                mRunnable = Runnable {
                    stopService(serviceIntent)
                }
                mHandler.postDelayed(
                    mRunnable, // Runnable
                    30000 // Delay in milliseconds
                )
            }

        })
    }

    companion object {
        const val APP_PREFERENCES = "appSettings"
        const val APP_PREFERENCES_IPADDR = "usedIpAddresses"
        var ipAddressUnit = arrayOf("ws://109.254.66.131:81", "ws://192.168.0.82:81")
    }
}