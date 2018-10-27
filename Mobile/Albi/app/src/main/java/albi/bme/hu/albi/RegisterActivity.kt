package albi.bme.hu.albi

import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun registerOnClick(view: View) {
        val nameEmpty = input_name.text.toString().isEmpty()
        val emailEmpty = input_email.text.toString().isEmpty()
        val passwordEmpty = input_password.text.toString().isEmpty()

        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(input_email.text.toString()).matches())) {
            input_email.error = "This is not a valid email format!"
        }

        if (nameEmpty) {
            input_name.error = "This field must be filled!"
        }
        if (emailEmpty) {
            input_email.error = "This field must be filled!"
        }
        if (passwordEmpty) {
            input_password.error = "This field must be filled!"
        }

        if (!nameEmpty && !emailEmpty && !passwordEmpty) {
            val newUser = User(input_name.text.toString(), input_email.text.toString(), input_password.text.toString(), input_phone_number.text.toString())
            sendNetworkRequestRegister(newUser)
        }
    }


    private fun sendNetworkRequestRegister(user: User) {
        val client = RestApiFactory.createUserClient()
        val call = client.createOrUpdateUser(user)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: retrofit2.Call<String>, response: Response<String>) {
                if (response.body() == "The username is already taken.") {
                    input_name.error = response.body()
                }
            }

            override fun onFailure(call: retrofit2.Call<String>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(this@RegisterActivity, "error in register :(" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
    }


}
