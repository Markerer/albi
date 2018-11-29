package albi.bme.hu.albi

import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.LoginResponse
import albi.bme.hu.albi.network.RestApiFactory
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {

    private val DELAY_IN_MILLIS: Long = 250

    var loggedUser: User? = null
    var done = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener(::loginOnClickListener)
        btnRegister.setOnClickListener(::registerOnClickListener)

    }

    private fun loginOnClickListener(view: View) {
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

    private fun registerOnClickListener(view: View) {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getEveryDetailOfUser(name: String) {
        val client = RestApiFactory.createUserClient()
        val call = client.getUserByUserName(name)

        /**
         * meg kell oldani a következő problémát:
         * amikor lekérjük a user további adatait (details)
         * az új szálban indul el, emiatt a
         * getEveryDetailOfUser(user.username) utáni sorban lévő
         * Handler még nem kap semmiféle konkrét visszatérési értéket
         * szóval meg kell oldani, hogy az addig ne tudjon elindulni, még ez
         * be nem fejezte a lekérést.
         */
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                loggedUser = response.body()
                done = true
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

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: retrofit2.Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body()?.message == "OK") {
                    getEveryDetailOfUser(user.username)
                    /**
                     * "időzített" késleltetés, hogy a másik szálból
                     * legyen ideje visszatérni az adatokkal
                     */
                    thread(start = true) {
                        while (!done){
                            Thread.sleep(1000)
                            done = true
                        }
                    }.join()
                    val handler = Handler()
                    handler.postDelayed({
                        User.token = "Bearer " + response.body()?.token
                        startMain(loggedUser)
                    }, DELAY_IN_MILLIS)

                } else {
                    Toast.makeText(this@LoginActivity, "Username or password is wrong!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LoginActivity, "error in login:(" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun startMain(user: User?) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }
}
