package albi.bme.hu.albi

import albi.bme.hu.albi.fragments.HouseDetailFragment
import albi.bme.hu.albi.fragments.addhouse.AddFlatFragment
import android.support.v7.app.AppCompatActivity
import albi.bme.hu.albi.model.User
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import albi.bme.hu.albi.fragments.profile.MyAdvertisementsFragment
import albi.bme.hu.albi.fragments.profile.ProfileFragment
import albi.bme.hu.albi.fragments.search.SearchFragment
import android.content.Context
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var houseDetail: HouseDetailFragment? = null
    private var profileDetail: ProfileFragment? = null
    private var searchFragment: SearchFragment? = null
    private var addFlatFragment: AddFlatFragment? = null
    private var myAdvertisementsFragment: MyAdvertisementsFragment? = null
    var user: User? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_house -> {
                replaceFragment(houseDetail!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_house -> {
                addFlatFragment!!.user = user!!
                replaceFragment(addFlatFragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
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

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user = intent.getSerializableExtra("user") as? User

        //Code from here is copied from the NavigationDrawerTest
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        //From here is our code
        houseDetail = HouseDetailFragment()
        searchFragment = SearchFragment()
        addFlatFragment = AddFlatFragment()
        profileDetail = ProfileFragment()
        myAdvertisementsFragment = MyAdvertisementsFragment()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(houseDetail!!)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                profileDetail!!.setUserInProfile(user!!)
                replaceFragment(profileDetail!!)
            }
            R.id.nav_my_ads -> {
                myAdvertisementsFragment!!.user = user
                replaceFragment(myAdvertisementsFragment!!)
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
