package albi.bme.hu.albi.fragments

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.FlatPageResponse
import albi.bme.hu.albi.network.ImageDataResponse
import albi.bme.hu.albi.network.RestApiFactory
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

class HouseDetailFragment : Fragment() {

    var usersData = ArrayList<Flat>()
    private var recyclerView: RecyclerView? = null

    private var pageNum = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.house_detail_fragment, container, false)

        pageNum = 1

        recyclerView = view.findViewById(R.id.rvHouseDetail)
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    networkRequestForPaging()
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
        })

        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            networkRequestForPaging()
            val handler = Handler()
            handler.postDelayed({ swipeContainer.isRefreshing = false }, 1000)
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light)
        initializationRecycle()

        return view
    }

    private fun initializationRecycle() {
        recyclerView?.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
        networkRequestForPaging()
    }

    private fun networkRequestForImagesIDs(flat: Flat) {
        val client = RestApiFactory.createFlatClient()
        val call = client.getImagesIDForFlatID(flat._id)

        call.enqueue(object : Callback<List<ImageDataResponse>> {
            override fun onResponse(call: Call<List<ImageDataResponse>>, response: Response<List<ImageDataResponse>>) {
                val imageIDs: List<ImageDataResponse>? = response.body()
                val actualFlatImageData = imageIDs as ArrayList<ImageDataResponse>

                if (actualFlatImageData.size != 0) {
                    for (j in actualFlatImageData.indices) {
                        flat.imageNames!!.add(actualFlatImageData[j].filename)
                    }
                    actualFlatImageData.clear()
                }
                recyclerView?.adapter?.notifyItemChanged(usersData.indexOf(flat))
            }

            override fun onFailure(call: Call<List<ImageDataResponse>>, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForImagesIDs()" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun networkRequestForPaging() {
        val client = RestApiFactory.createFlatClient()
        val call = client.getMainFlatsByPage(pageNum)

        call.enqueue(object : Callback<FlatPageResponse> {
            override fun onFailure(call: Call<FlatPageResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<FlatPageResponse>, response: Response<FlatPageResponse>) {
                val flatResponse = response.body()!!

                if (pageNum == 1) {
                    usersData = flatResponse.docs as ArrayList<Flat>

                    for (i in usersData.indices) {
                        networkRequestForImagesIDs(usersData[i])
                    }

                    val adapter = RecyclerAdapter(usersData, context!!)
                    recyclerView?.adapter = adapter
                } else {
                    if (pageNum <= flatResponse.pages!!) {
                        val flats = flatResponse.docs as ArrayList<Flat>
                        usersData.addAll(flats)

                        for (i in flats.indices) {
                            networkRequestForImagesIDs(flats[i])
                        }
                    }
                }
                pageNum++
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
                    networkRequestForImagesIDs(usersData[i])
                }

                val adapter = RecyclerAdapter(usersData, context!!)
                recyclerView?.adapter = adapter
            }

            override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForMainFlats()" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}

