package albi.bme.hu.albi

import albi.bme.hu.albi.interfaces.user.UserClient
import albi.bme.hu.albi.model.User
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class LoginActivity : AppCompatActivity() {

    private val DELAY_IN_MILLIS: Long = 500

    var loggedUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        var user: User?

        btnLogin.setOnClickListener {

            if (etUsername.text.toString().isEmpty()) {
                etUsername.requestFocus()
                etUsername.error = "Please enter your email address"
            }
            if (etPassword.text.toString().isEmpty()) {
                etPassword.requestFocus()
                etPassword.error = "Please enter your password"
            }
            if (!etUsername.text.toString().isEmpty() && !etPassword.text.toString().isEmpty() && !logged) {
                user = User(etUsername.text.toString(), etPassword.text.toString())
                sendNetworkRequestLogin(user!!)
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getEveryDetailOfUser(name: String) {
        val gson = GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .create()

        val builder = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit = builder.build()
        val client = retrofit.create(UserClient::class.java)
        val call = client.getUserByUserName(name)


        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                loggedUser = response.body()?.get(0)
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LoginActivity, "error in finding user:(" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun sendNetworkRequestLogin(user: User) {
        val gson = GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .create()

        val builder = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit = builder.build()
        val client = retrofit.create(UserClient::class.java)
        val call = client.loginUser(user)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: retrofit2.Call<String>, response: Response<String>) {
                if (response.body().toString() == "OK") {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    getEveryDetailOfUser(user.username)
                    val handler = Handler()
                    handler.postDelayed({
                        intent.putExtra("user", loggedUser)
                        startActivity(intent)
                        finish()
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

}
