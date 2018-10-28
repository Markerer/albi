package albi.bme.hu.albi.fragments.fragments.mainview.profile

import albi.bme.hu.albi.R
import albi.bme.hu.albi.fragments.fragments.mainview.ProfileUpdateDialogFragment
import albi.bme.hu.albi.model.User
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ProfileFragment: Fragment() {

    var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        val userName = view.findViewById<TextView>(R.id.userName)
        val userEmail = view.findViewById<TextView>(R.id.profile_email)
        val phoneNumber = view.findViewById<TextView>(R.id.tvphone_number)
        val editButton = view.findViewById<Button>(R.id.editButton);
        userName.text = user?.username
        userEmail.text = user?.email
        phoneNumber.text = user?.phone_number


        editButton.setOnClickListener {
            val dialog = ProfileUpdateDialogFragment()
            dialog.user = user!!
            dialog.show(activity?.supportFragmentManager, "")

        }

        return view
    }



    public fun setUserInProfile(user: User){
        this.user = user
    }
}