package albi.bme.hu.albi.fragments.profile

import albi.bme.hu.albi.R
import albi.bme.hu.albi.fragments.mainview.ProfileUpdateDialogFragment
import albi.bme.hu.albi.model.User
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ProfileFragment : Fragment() {

    var user: User? = null

    private lateinit var userName : TextView
    private lateinit var userEmail: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var address: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        userName = view.findViewById(R.id.userName)
        userEmail = view.findViewById(R.id.profile_email)
        phoneNumber = view.findViewById(R.id.tvphone_number)
        address = view.findViewById(R.id.tvaddress)
        val editButton = view.findViewById<Button>(R.id.editButton)
        setView()


        editButton.setOnClickListener {
            val dialog = ProfileUpdateDialogFragment()
            dialog.user = user!!
            dialog.profileFragment = this
            dialog.show(activity?.supportFragmentManager, "")
        }
        return view
    }

    fun setView(){
        userName.text = user?.username
        userEmail.text = user?.email
        phoneNumber.text = user?.phone_number
        address.text = user?.address
    }

    fun setUserInProfile(user: User) {
        this.user = user
    }
}