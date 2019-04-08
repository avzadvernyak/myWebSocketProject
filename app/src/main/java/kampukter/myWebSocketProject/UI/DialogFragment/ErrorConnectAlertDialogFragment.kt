package kampukter.myWebSocketProject.UI.DialogFragment

import androidx.fragment.app.DialogFragment
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kampukter.myWebSocketProject.R


class ErrorConnectAlertDialogFragment : DialogFragment() {

    private lateinit var myDialogInterfaceListener : DialogInterface.OnClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.connectErrorTitle))
            .setMessage(arguments?.getString(ARG_MESSAGE))
            .setCancelable(false)
            .setNeutralButton(android.R.string.cancel,myDialogInterfaceListener)
            .create()
    }
    private fun setOnDialogInterfaceListener(arg: DialogInterface.OnClickListener){
        myDialogInterfaceListener = arg
    }

    companion object {
        const val TAG = "ErrorConnectAlertDialogFragment"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        fun create(message: String, myListener: DialogInterface.OnClickListener ): ErrorConnectAlertDialogFragment =
            ErrorConnectAlertDialogFragment().apply {
                setOnDialogInterfaceListener(myListener)
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
    }
}