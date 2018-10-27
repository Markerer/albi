package albi.bme.hu.albi.fragments.fragments.mainview

import albi.bme.hu.albi.R
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
import kotlinx.android.synthetic.main.profile_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdateDialogFragment : DialogFragment() {

    private lateinit var name : EditText
    private lateinit var email : EditText
    private lateinit var phone : EditText
    var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
                .setTitle("Edit Profile")
                .setView(getContentView())
                .setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    if (isValid()){
                        updateProfile()
                        networkRequestForProfileUpdate()
                    }
                }.setNegativeButton("Cancel", null)
                .create()
    }

    private fun updateProfile(){
        user!!.username = name.text.toString()
        user!!.email = email.text.toString()
        user!!.phone_number = phone.text.toString()
    }

    private fun networkRequestForProfileUpdate() {
        val client = RestApiFactory.createUserClient()
        val call = client.createOrUpdateUser(user!!)

        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }
        })

    }
    private fun isValid() : Boolean {
        return (name.text.isNotEmpty() && email.text.isNotEmpty() && phone.text.isNotEmpty())
    }

    private fun getContentView() : View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.profile_update_dialogfragment, null)
        name = contentView.findViewById(R.id.editName)
        email = contentView.findViewById(R.id.editEmail)
        phone = contentView.findViewById(R.id.editPhone)

        name.setText(user!!.username)
        email.setText(user!!.email)
        phone.setText(user!!.phone_number)

        return contentView
    }
}