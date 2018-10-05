package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
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

    var currentFragment : Fragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        removeFragment(currentFragment)
        when (item.itemId) {
            R.id.navigation_house -> {
                InitializationRecycle()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add_house -> {
                supportFragmentManager.popBackStack()
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

    private fun InitializationRecycle() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycleView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val usersData = ArrayList<Flat>()
        usersData.add(Flat("Kiadó 3 ágyas szoba az II. kerületben",
                "2",
                "Description1",
                20000)
        )

        usersData.add(Flat("Kiadó 2 ágyas szoba az I. kerületben",
                "2",
                "Description2",
                10000)
        )

        usersData.add(Flat("Kiadó 1 ágyas szoba az IV. kerületben",
                "4",
                "Description3",
                12000)
        )

        usersData.add(Flat("Kiadó 1 ágyas szoba az IV. kerületben",
                "1",
                "Description4",
                21000)
        )

        usersData.add(Flat("Kiadó 8 ágyas szoba az V. kerületben",
                "3",
                "Description5",
                6700)
        )

        usersData.add(Flat("Kiadó 6 ágyas szoba az XV. kerületben",
                "2",
                "Description6",
                12000)
        )

        val adapter = RecyclerAdapter(usersData)
        recyclerView.adapter = adapter
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
    }

}
