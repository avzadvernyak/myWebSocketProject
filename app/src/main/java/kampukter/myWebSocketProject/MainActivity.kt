package kampukter.myWebSocketProject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("blablabla", "START MainActivity")
        if (savedInstanceState == null) supportFragmentManager.beginTransaction().add(
            android.R.id.content,
            MainFragment()
        ).commit()
    }
}