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

    var usersData = ArrayList<Flat>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initializationRecycle()
    }

    private fun initializationRecycle(): RecyclerView? {
        val recyclerView = RecyclerView(context!!)
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)

        usersData.add(Flat(123210, "Pannonia utca", 3))
        usersData.add(Flat(12232100, "Juharfa utca", 3))
        usersData.add(Flat(12321200, "Gyöngyvirág utca", 3))
        usersData.add(Flat(1322200, "Eke utca", 3))
        usersData.add(Flat(1232200, "Szekér utca", 3))
        usersData.add(Flat(1223200, "Ló utca", 3))
        usersData.add(Flat(1233200, "Gomb utca", 3))
        usersData.add(Flat(1322200, "Kacsa utca", 3))
        usersData.add(Flat(1223200, "Fa utca", 3))
        usersData.add(Flat(1232200, "Szamár utca", 3))
        usersData.add(Flat(1223200, "Kapa utca", 3))
        usersData.add(Flat(12123200, "Pannonia utca", 3))


        val adapter = RecyclerAdapter(usersData)
        recyclerView.adapter = adapter
        return recyclerView
    }


    public fun setFlatsData(listOfFlats: List<Flat>){
        this.usersData = ArrayList(listOfFlats)
    }

}