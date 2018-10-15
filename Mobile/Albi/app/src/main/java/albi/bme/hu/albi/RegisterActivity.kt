package albi.bme.hu.albi

import albi.bme.hu.albi.interfaces.user.UserClient
import albi.bme.hu.albi.model.User
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun registerOnClick(view: View){
        var nameEmpty = input_name.text.toString().isEmpty()
        var emailEmpty = input_email.text.toString().isEmpty()
        var passwordEmpty = input_password.text.toString().isEmpty()


        if(nameEmpty){
            input_name.error = "must be filled"
        }
        if(emailEmpty){
            input_email.error = "must be filled"
        }
        if(passwordEmpty){
            input_password.error = "must be filled"
        }

        if (!nameEmpty && !emailEmpty && !passwordEmpty){
            val newUser = User(input_name.text.toString(), input_email.text.toString(), input_password.text.toString(), input_phone_number.text.toString())
            sendNetworkRequestRegister(newUser)
        }
    }


    private fun sendNetworkRequestRegister(user: User) {
        val builder = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        val client = retrofit.create(UserClient::class.java)
        val call = client.createNewUser(user)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: retrofit2.Call<String>, response: Response<String>) {
                Toast.makeText(this@RegisterActivity, "no error in creating new user: " + response.body().toString(), Toast.LENGTH_LONG)
            }

            override fun onFailure(call: retrofit2.Call<String>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(this@RegisterActivity, "error in register :(" + t?.message, Toast.LENGTH_LONG)
            }
        })
    }
}
