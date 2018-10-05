package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.fragments.fragments.mainview.HouseDetailFragment
import albi.bme.hu.albi.fragments.fragments.profile.LoginFragment
import albi.bme.hu.albi.model.Flat
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentFragment : Fragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        removeFragment(currentFragment)
        when (item.itemId) {
            R.id.navigation_house -> {
                currentFragment = HouseDetailFragment()
                loadFragment(currentFragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_house -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                currentFragment = LoginFragment()
                loadFragment(currentFragment!!)
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

    private fun removeFragment(fragment: Fragment?){
        if (fragment == null) return
        supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        currentFragment=HouseDetailFragment()
        loadFragment(currentFragment!!)
    }

}
