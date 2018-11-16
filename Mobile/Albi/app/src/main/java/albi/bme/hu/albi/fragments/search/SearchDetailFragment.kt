package albi.bme.hu.albi.fragments.search

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

@SuppressLint("ValidFragment")
class SearchDetailFragment(val data:ArrayList<Flat>): Fragment() {

    var detailsData = data
    var recycleView: RecyclerView? = null
    private var pageNum = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_detail_fragment, container, false)

        recycleView = view.findViewById(R.id.rv_search_detail)
        recycleView?.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
        detailsData = data
        val adapter = RecyclerAdapter(detailsData, context!!, false)
        recycleView!!.adapter = adapter
        recycleView!!.adapter!!.notifyDataSetChanged()

        return view
    }

    public fun loadData(list: ArrayList<Flat>){
        this.detailsData = list
    }

}