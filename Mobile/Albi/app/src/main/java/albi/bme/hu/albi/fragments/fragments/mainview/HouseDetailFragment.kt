package albi.bme.hu.albi.fragments.fragments.mainview

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class HouseDetailFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initializationRecycle()


    }

    private fun initializationRecycle(): RecyclerView {
        val recyclerView = RecyclerView(context!!)
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)

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
        return recyclerView
    }
}