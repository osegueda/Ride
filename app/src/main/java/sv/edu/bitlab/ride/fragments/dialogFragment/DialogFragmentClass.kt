package sv.edu.bitlab.ride.fragments.dialogFragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import sv.edu.bitlab.ride.R


public class DialogFragmentClass: DialogFragment() {

    companion object {

        @JvmStatic
        fun newInstance() = DialogFragmentClass()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog,container,false)
    }


}