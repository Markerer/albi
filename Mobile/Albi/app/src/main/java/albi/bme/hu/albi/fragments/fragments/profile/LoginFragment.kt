package albi.bme.hu.albi.fragments.fragments.profile

import albi.bme.hu.albi.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class LoginFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, null)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val etEmailAddress = view.findViewById<EditText>(R.id.etEmailAddress)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        btnLogin.setOnClickListener {
            if (etEmailAddress.text.toString().isEmpty()) {
                etEmailAddress.requestFocus()
                etEmailAddress.error = "Please enter your email address"
            }
            if (etPassword.text.toString().isEmpty()) {
                etPassword.requestFocus()
                etPassword.error = "Please enter your password"
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}