package kampukter.myWebSocketProject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Sensor1Information : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) supportFragmentManager.beginTransaction().add(
            android.R.id.content,
            Sensor1InformationFragment()
        ).commit()
    }

}