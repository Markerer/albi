package albi.bme.hu.albi

import albi.bme.hu.albi.fragments.fragments.mainview.HouseDetailFragment
import albi.bme.hu.albi.fragments.fragments.profile.LoginFragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var login: LoginFragment? = null
    var houseDetail: HouseDetailFragment? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_house -> {
                replaceFragment(houseDetail!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_house -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                replaceFragment(login!!)
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

        login = LoginFragment()
        houseDetail = HouseDetailFragment()

        /*val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener{
            SwipeRefreshLayout.OnRefreshListener {
                // TODO onRefresh() --> behúzni az adatokat
                @Override
                fun onRefresh() {
                    Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show()
                }
            }
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)*/


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(login!!)

    }

}
