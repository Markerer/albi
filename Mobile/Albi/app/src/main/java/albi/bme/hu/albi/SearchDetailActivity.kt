package albi.bme.hu.albi

import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.fragments.search.SearchResult
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.FlatPageResponse
import albi.bme.hu.albi.network.RestApiFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView:  RecyclerView

    private lateinit var searchResult: SearchResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_detail)

        val bundle = intent.extras!!
        searchResult = bundle.getSerializable("result") as SearchResult

        recyclerView = findViewById(R.id.rvSearchResult)
        recyclerView.layoutManager = LinearLayoutManager(this@SearchDetailActivity, LinearLayout.VERTICAL, false)

        sendNetworkRequestSearch()

    }

    private fun sendNetworkRequestSearch(){
        val client = RestApiFactory.createFlatClient()
        val call = client.getFlatsBySearch(1,
                searchResult.price,
                searchResult.numberOfRooms,
                searchResult.address)


        call.enqueue(object: Callback<FlatPageResponse> {
            override fun onFailure(call: Call<FlatPageResponse>, t: Throwable) {
                Toast.makeText(this@SearchDetailActivity, "Something is not okay with the server (again)\n" + t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FlatPageResponse>, response: Response<FlatPageResponse>) {
                val adapter = RecyclerAdapter( response.body()?.docs as ArrayList<Flat>, this@SearchDetailActivity, false)
                recyclerView.adapter = adapter
            }
        })
    }

}