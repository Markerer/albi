package albi.bme.hu.albi.fragments

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.FlatPageResponse
import albi.bme.hu.albi.network.ImageDataResponse
import albi.bme.hu.albi.network.RestApiFactory
import albi.bme.hu.albi.network.RestApiList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler

class HouseDetailFragment : Fragment(), RestApiList.ListInterface {

    var usersData = ArrayList<Flat>()
    private lateinit var recyclerView: RecyclerView

    private var pageNum = 1

    private val restApiList = RestApiList(this)
    override fun photoLoaded(flat: Flat) {
        recyclerView.adapter?.notifyItemChanged(usersData.indexOf(flat))
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.house_detail_fragment, container, false)

        pageNum = 1

        recyclerView = view.findViewById(R.id.rvHouseDetail)
        initializationRecycle()

        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            pageNum = 1
            usersData.clear()
            networkRequestForPaging()
            val handler = Handler()
            handler.postDelayed({ swipeContainer.isRefreshing = false }, 1000)
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light)


        return view
    }

    private fun initializationRecycle() {
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
        recyclerView.adapter = RecyclerAdapter(usersData, context!!, owner = false)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (pageNum != -1 && !recyclerView.canScrollVertically(1)) {
                    networkRequestForPaging()
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
        })
        networkRequestForPaging()
    }

    private fun networkRequestForPaging() {
        if (pageNum == -1)
            return
        val client = RestApiFactory.createFlatClient()
        val call = client.getMainFlatsByPage(pageNum)

        call.enqueue(object : Callback<FlatPageResponse> {
            override fun onFailure(call: Call<FlatPageResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Server not responding", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<FlatPageResponse>, response: Response<FlatPageResponse>) {
                val flatResponse = response.body()!!
                val flats = flatResponse.docs!! as ArrayList<Flat>

                for (i in flats.indices){
                    restApiList.networkRequestForImagesIDs(flats[i])
                }
                usersData.addAll(flats)
                pageNum++
                if (pageNum > flatResponse.pages!!){
                    pageNum = -1
                }
            }
        })
    }

    private fun networkRequestForMainFlats() {
        val client = RestApiFactory.createFlatClient()
        val call = client.getMainFlats()

        call.enqueue(object : Callback<List<Flat>> {
            override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                val flats: List<Flat>? = response.body()
                usersData = flats as ArrayList<Flat>

                for (i in usersData.indices) {
                    restApiList.networkRequestForImagesIDs(usersData[i])
                }

                val adapter = RecyclerAdapter(usersData, context!!, false)
                recyclerView?.adapter = adapter
            }

            override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForMainFlats()" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}

