package albi.bme.hu.albi.fragments.fragments.profile

import albi.bme.hu.albi.R
import albi.bme.hu.albi.fragments.fragments.mainview.HouseDetailFragment
import albi.bme.hu.albi.interfaces.user.UserClient
import albi.bme.hu.albi.model.User
import android.os.Bundle
import android.support.v4.app.Fragment
import android.telecom.Call
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

var logged = false

class LoginFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, null)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val etEmailAddress = view.findViewById<EditText>(R.id.etEmailAddress)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val user = User(etEmailAddress.text.toString(), etPassword.text.toString())

        btnLogin.setOnClickListener {

            if (etEmailAddress.text.toString().isEmpty()) {
                etEmailAddress.requestFocus()
                etEmailAddress.error = "Please enter your email address"
            }
            if (etPassword.text.toString().isEmpty()) {
                etPassword.requestFocus()
                etPassword.error = "Please enter your password"
            }
            if(!etEmailAddress.text.toString().isEmpty() && !etPassword.text.toString().isEmpty() && !logged){
                // TODO login után ne hozza egyből létre a fragmentet, hanem ellenőrizze h sikeres e a belépés
                activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, HouseDetailFragment())
                        .commit()
                sendNetworkRequestLogin(user)
            }
        }

        btnRegister.setOnClickListener {
            //val user1 = User("test", etEmailAddress.text.toString(), etPassword.text.toString(), "+361234567")
            //user = User(etEmailAddress.text.toString(), etPassword.text.toString())
//            val user3 = User("test", "123456")
            sendNetworkRequestRegister(user)

        }

        return view
    }

    private fun sendNetworkRequestLogin(user: User){
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
                if(response.body().toString().equals("OK")){
                    logged = true
                    Toast.makeText(activity, "login is: " + response.body().toString(), Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: retrofit2.Call<String>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(activity, "error in login:("+t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun sendNetworkRequestRegister(user: User){
        val builder = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder.build()
        val client = retrofit.create(UserClient::class.java)
        val call  = client.createNewUser(user)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: retrofit2.Call<User>, response: Response<User>) {
                Toast.makeText(activity, "no error in creating new user: " + response.body().toString(), Toast.LENGTH_LONG)
            }

            override fun onFailure(call: retrofit2.Call<User>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "error in register :("+t?.message, Toast.LENGTH_LONG)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}