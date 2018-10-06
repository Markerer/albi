package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.fragments.fragments.mainview.HouseDetailFragment
import albi.bme.hu.albi.fragments.fragments.profile.LoginFragment
import albi.bme.hu.albi.interfaces.FlatClient
import albi.bme.hu.albi.model.Flat
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
//import jdk.nashorn.internal.objects.NativeFunction.call
import retrofit2.Response


class MainActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_house -> {
                replaceFragment(HouseDetailFragment())

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_house -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                replaceFragment(LoginFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(R.id.frame, fragment)
                .commit()
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(LoginFragment())

        val builder: Retrofit.Builder = Retrofit.Builder()
                .baseUrl("http://WeNeedThis1235678.com/")
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()
        val client: FlatClient = retrofit.create(FlatClient::class.java)
        val call = client.getMainFlats()



        call.enqueue(object : Callback<List<Flat>> {
            override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                val flats: List<Flat>? = response.body()

                //listView.setAdapter(GitHubRepoAdapter(this@MainActivity, repos))

                val rv = findViewById<RecyclerView>(R.id.rv)
                rv.adapter = RecyclerAdapter(flats as ArrayList<Flat>)
            }

            override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "error :(", Toast.LENGTH_LONG).show()
            }
        })

    }




}
