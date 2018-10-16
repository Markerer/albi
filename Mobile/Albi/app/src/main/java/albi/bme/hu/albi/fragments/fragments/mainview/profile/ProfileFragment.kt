package albi.bme.hu.albi.fragments.fragments.mainview.profile

import albi.bme.hu.albi.R
import albi.bme.hu.albi.model.User
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ProfileFragment: Fragment() {

    var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        var userName = view.findViewById<TextView>(R.id.userName)
        val userEmail = view.findViewById<TextView>(R.id.profile_email)
        userName.text = user?.username
        userEmail.text = user?.email
        return view
    }

    public fun setUserInProfile(user: User){
        this.user = user
    }
}