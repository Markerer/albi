package albi.bme.hu.albi.fragments.mainview

import albi.bme.hu.albi.R
import albi.bme.hu.albi.fragments.profile.ProfileFragment
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdateDialogFragment : DialogFragment() {

    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var address: EditText
    var user: User? = null

    var profileFragment: ProfileFragment? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setTitle("Edit Profile")
                .setView(getContentView())
                .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                    if (isValid()) {
                        updateProfile()
                        networkRequestForProfileUpdate()
                    }
                }.setNegativeButton("Cancel", null)
                .create()
    }

    private fun updateProfile() {
        user!!.email = email.text.toString()
        user!!.phone_number = phone.text.toString()
        user!!.address = address.text.toString()
    }

    private fun networkRequestForProfileUpdate() {
        val client = RestApiFactory.createUserClient()
        val call = client.updateUser(user!!, User.token!!)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Error: " + t.message, Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        profileFragment?.setView()
    }

    private fun isValid(): Boolean {
        return ((email.text.toString() != user!!.email ||
                phone.text.toString() != user!!.phone_number ||
                address.text.toString() != user!!.address) &&
                (email.text.isNotEmpty() && phone.text.isNotEmpty() && address.text.isNotEmpty()))
    }

    private fun getContentView(): View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.profile_update_dialogfragment, null)
        email = contentView.findViewById(R.id.editEmail)
        phone = contentView.findViewById(R.id.editPhone)
        address = contentView.findViewById(R.id.editAddress)

        email.setText(user!!.email)
        phone.setText(user!!.phone_number)
        address.setText(user!!.address)

        return contentView
    }
}