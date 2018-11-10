package albi.bme.hu.albi

import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.content.Intent
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
        val name = input_name.text.toString()
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        val phoneNumber = input_phone_number.text.toString()
        val address = input_address.text.toString()

        val nameEmpty = name.isEmpty()
        val emailEmpty = email.isEmpty()
        val passwordEmpty = password.isEmpty()

        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
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
            val newUser = User(name, email, password, phoneNumber, address)
            sendNetworkRequestRegister(newUser)
        }
    }

    private fun sendNetworkRequestRegister(user: User) {
        val client = RestApiFactory.createUserClient()
        val call = client.createUser(user)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: retrofit2.Call<User>, response: Response<User>) {
                startMain(response.body())
            }

            override fun onFailure(call: retrofit2.Call<User>?, t: Throwable?) {
                input_name.error = "The username is already taken."
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startMain(user: User?){
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }
}
