package kampukter.myWebSocketProject.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kampukter.myWebSocketProject.R
import kotlinx.android.synthetic.main.unit_list_activity.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.unit_list_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Root"
        }


        Log.d("blablabla", "START MainActivity")
        viewPager.adapter = UnitPagerAdapter(supportFragmentManager)
        /*
        if (savedInstanceState == null) supportFragmentManager.beginTransaction().add(
            android.R.id.content,
            MainFragment()
        ).commit()*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_fragment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
                )
    }
}