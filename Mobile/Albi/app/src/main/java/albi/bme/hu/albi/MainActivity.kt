package albi.bme.hu.albi

import albi.bme.hu.albi.fragments.fragments.mainview.HouseDetailFragment
import albi.bme.hu.albi.fragments.fragments.mainview.profile.ProfileFragment
import albi.bme.hu.albi.fragments.fragments.mainview.search.SearchFragment
import albi.bme.hu.albi.model.User
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import albi.bme.hu.albi.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

var logged = false

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var houseDetail: HouseDetailFragment? = null
    var profileDetail: ProfileFragment? = null
    var searchFragment: SearchFragment? = null
    var user : User? = null


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
                searchFragment = SearchFragment()
                replaceFragment(searchFragment!!)
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

        //Code from here is copied from the NavigationDrawerTest bullshit
        setSupportActionBar(toolbar)

        user = intent.getSerializableExtra("user") as User

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        //From here is our code
        houseDetail = HouseDetailFragment()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(houseDetail!!)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                profileDetail = ProfileFragment()
                profileDetail!!.setUserInProfile(user as User)
                replaceFragment(profileDetail!!)
            }
            R.id.nav_my_ads -> {

            }
            R.id.nav_fav_ads -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}
