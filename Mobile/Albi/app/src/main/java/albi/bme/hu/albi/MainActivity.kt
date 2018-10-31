package albi.bme.hu.albi


//TODO: 1)fénykép feltöltése
//TODO: 2)lakás feltöltése (pipa)
//TODO: 3)kép megjelenítése mainben
//TODO: 4)profil szerkesztése (pipa)
//TODO: 5)saját lakások megtekintése  -- profilban lehetne egy recycler, ahol szerepel az összes, ott lehet egyesével megnézni
//TODO: layout mappa szelektálása--> https://stackoverflow.com/questions/4930398/can-the-android-layout-folder-contain-subfolders

//Kérdések:
//Auto bejelentkeztetés jó-e simán id-vel?
//Fénykép feltöltése hogyan?
//Fénykép megjelenítése?


//TODO: paging (fml)

import android.support.v7.app.AppCompatActivity
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
import android.view.MenuItem
import albi.bme.hu.albi.fragments.fragments.mainview.addhouse.AddFlatFragment
import albi.bme.hu.albi.fragments.fragments.mainview.profile.MyAdvertisementsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

const val PREF_NAME: String = "AlbiSettings"
const val CURRENT_USER_KEY = "currentUser"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var houseDetail: HouseDetailFragment? = null
    var profileDetail: ProfileFragment? = null
    var searchFragment: SearchFragment? = null
    var addFlatFragment: AddFlatFragment? = null
    var myAdvertisementsFragment: MyAdvertisementsFragment? = null
    var user: User? = null


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_house -> {
                replaceFragment(houseDetail!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_house -> {
                replaceFragment(addFlatFragment!!)
                addFlatFragment!!.user = user!!
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

        // java.lang.RuntimeException: Unable to start activity ComponentInfo{albi.bme.hu.albi/albi.bme.hu.albi.MainActivity}: kotlin.TypeCastException: null cannot be cast to non-null type albi.bme.hu.albi.model.User
        // https://stackoverflow.com/questions/37949024/kotlin-typecastexception-null-cannot-be-cast-to-non-null-type-com-midsizemango
        user = intent.getSerializableExtra("user") as? User


        //Code from here is copied from the NavigationDrawerTest bullshit
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

    private fun saveUserId(user: User?) {
        val sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(CURRENT_USER_KEY, user?._id)
        editor.apply()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_profile -> {
                profileDetail!!.setUserInProfile(user!!)
                replaceFragment(profileDetail!!)
            }
            R.id.nav_my_ads -> {
                myAdvertisementsFragment!!.user = user
                replaceFragment(myAdvertisementsFragment!!)
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
