package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.fragments.search.SearchResult
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.FlatPageResponse
import albi.bme.hu.albi.network.RestApiFactory
import albi.bme.hu.albi.network.RestApiList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDetailActivity : AppCompatActivity(), RestApiList.ListInterface {

    private lateinit var recyclerView: RecyclerView
    private var usersData = ArrayList<Flat>()
    private lateinit var searchResult: SearchResult

    private var pageNum = 1

    private val restApiList = RestApiList(this)
    override fun photoLoaded(flat: Flat) {
        recyclerView.adapter?.notifyItemChanged(usersData.indexOf(flat))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        val bundle = intent.extras!!
        searchResult = bundle.getSerializable("result") as SearchResult

        recyclerView = findViewById(R.id.rvSearchResult)
        recyclerView.layoutManager = LinearLayoutManager(this@SearchDetailActivity, LinearLayout.VERTICAL, false)
        recyclerView.adapter = RecyclerAdapter(usersData, this@SearchDetailActivity, owner = false)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    sendNetworkRequestSearch()
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
        })

        sendNetworkRequestSearch()

    }

    private fun sendNetworkRequestSearch() {
        if (pageNum == -1)
            return

        val client = RestApiFactory.createFlatClient()
        val call = client.getFlatsBySearch(pageNum,
                searchResult.price,
                searchResult.numberOfRooms,
                searchResult.address)


        call.enqueue(object : Callback<FlatPageResponse> {
            override fun onFailure(call: Call<FlatPageResponse>, t: Throwable) {
                Toast.makeText(this@SearchDetailActivity, "Something is not okay with the server (again)\n" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FlatPageResponse>, response: Response<FlatPageResponse>) {
                val flatResponse = response.body()!!
                val newData = flatResponse.docs as ArrayList<Flat>

                for (i in newData.indices) {
                    if (newData[i].forSale == searchResult.forSale) {
                        restApiList.networkRequestForImagesIDs(newData[i])
                        usersData.add(newData[i])
                    }
                }

                pageNum++

                if (usersData.size == 0 && pageNum <= flatResponse.pages!!){
                    sendNetworkRequestSearch()
                }

                if (pageNum > flatResponse.pages!!) {
                    pageNum = -1
                }

            }
        })
    }

}
