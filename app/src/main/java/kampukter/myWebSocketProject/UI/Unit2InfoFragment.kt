package kampukter.myWebSocketProject.UI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kampukter.myWebSocketProject.R
import kotlinx.android.synthetic.main.sensor2_fragment.*

class Unit2InfoFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionFAB.setOnClickListener {
            startActivity(Intent(activity, LocationActivity::class.java))
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sensor2_fragment, container, false)
    }

    companion object {
        fun newInstance(): Unit2InfoFragment {
            return Unit2InfoFragment()
        }
    }

}