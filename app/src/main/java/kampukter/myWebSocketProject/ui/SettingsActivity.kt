package kampukter.myWebSocketProject.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kampukter.myWebSocketProject.R
import kampukter.myWebSocketProject.UnitSensorApplication.Companion.APP_PREFERENCES
import kampukter.myWebSocketProject.UnitSensorApplication.Companion.APP_PREFERENCES_IPADDR
import kampukter.myWebSocketProject.UnitSensorApplication.Companion.ipAddressUnit
import kotlinx.android.synthetic.main.settings_activity_view.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appSharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        setContentView(R.layout.settings_activity_view)

        setSupportActionBar(editSettingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        firstIPTextInputEdit.setText(ipAddressUnit[0])
        secondIPTextInputEdit.setText(ipAddressUnit[1])

        saveSettingsFAB.setOnClickListener {

            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow( it .windowToken , 0)

            if (!firstIPTextInputEdit.text.isNullOrEmpty() && !secondIPTextInputEdit.text.isNullOrEmpty() && firstIPTextInputEdit.text != secondIPTextInputEdit.text) {
                val settingsForSave = HashSet<String>()
                settingsForSave.add(firstIPTextInputEdit.text.toString())
                settingsForSave.add(secondIPTextInputEdit.text.toString())
                appSharedPreferences.edit()
                    .putStringSet(APP_PREFERENCES_IPADDR, settingsForSave)
                    .apply()
                ipAddressUnit[0] = firstIPTextInputEdit.text.toString()
                ipAddressUnit[1] = secondIPTextInputEdit.text.toString()
                finish()
            } else Snackbar.make(
                editSettingsLayout, getString(R.string.errorInputIP),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}