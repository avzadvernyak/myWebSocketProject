package kampukter.myWebSocketProject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class WebSocketService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("blablabla", "Start service")
        if (flags and Service.START_FLAG_RETRY == 0) {
            // TODO Если это повторный запуск, выполнить какие-то действия.
        } else {
            // TODO Альтернативные действия в фоновом режиме.
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("blablabla", "Stop service")
    }
}
