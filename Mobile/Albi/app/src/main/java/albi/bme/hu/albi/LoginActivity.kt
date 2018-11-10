package albi.bme.hu.albi

import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val DELAY_IN_MILLIS: Long = 250

    var loggedUser: User? = null

    private fun checkIfLogged(){
        val sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        if (!sp.contains(CURRENT_USER_KEY)) return
        val currentId = sp.getString(CURRENT_USER_KEY, "")
        if (currentId != ""){
            searchForUserById(currentId!!)
        }
    }

    private fun searchForUserById(id: String){
        val client = RestApiFactory.createUserClient()
        val call = client.getUserById(id)

        call.enqueue( object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LoginActivity, "error in finding user by ID:(" + t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                startMain(response.body())
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkIfLogged()

        btnLogin.setOnClickListener (::loginOnClickListener)
        btnRegister.setOnClickListener (::registerOnClickListener)

    }

    private fun loginOnClickListener(view: View){
        val user: User?
        if (etUsername.text.toString().isEmpty()) {
            etUsername.requestFocus()
            etUsername.error = "Please enter your email address"
        }

        if (etPassword.text.toString().isEmpty()) {
            etPassword.requestFocus()
            etPassword.error = "Please enter your password"
        }
        if (!etUsername.text.toString().isEmpty() && !etPassword.text.toString().isEmpty()) {
            user = User(etUsername.text.toString(), etPassword.text.toString())
            sendNetworkRequestLogin(user)
        }
    }

    private fun registerOnClickListener(view: View){
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getEveryDetailOfUser(name: String) {
        val client = RestApiFactory.createUserClient()
        val call = client.getUserByUserName(name)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                loggedUser = response.body()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LoginActivity, "error in finding user:(" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun sendNetworkRequestLogin(user: User) {
        val client = RestApiFactory.createUserClient()
        val call = client.loginUser(user)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: retrofit2.Call<String>, response: Response<String>) {
                if (response.body().toString() == "OK") {
                    getEveryDetailOfUser(user.username)
                    val handler = Handler()
                    handler.postDelayed({
                        startMain(loggedUser)
                    }, DELAY_IN_MILLIS)
                } else {
                    Toast.makeText(this@LoginActivity, "Username or password is wrong!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LoginActivity, "error in login:(" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun startMain(user: User?){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }
}
