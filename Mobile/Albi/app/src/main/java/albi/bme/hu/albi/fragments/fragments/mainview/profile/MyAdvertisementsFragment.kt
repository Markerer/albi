package albi.bme.hu.albi.fragments.fragments.mainview.profile

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.RestApiFactory
import android.os.Bundle
import android.support.v4.app.Fragment
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

class MyAdvertisementsFragment : Fragment() {

    var myFlats: ArrayList<Flat>? = ArrayList<Flat>()
    var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_house_advertisements_fragment, container, false)
        val recycleView = view.findViewById<RecyclerView>(R.id.rvMyHouses)

        initializationRecycle(recycleView)

        return view
    }

    private fun initializationRecycle(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
        networkRequestForMyFlats(recyclerView)
    }

    private fun networkRequestForMyFlats(recyclerView: RecyclerView) {
        val client = RestApiFactory.createUserClient()
        val call = client.getMyFlats(user!!._id!!)

        call.enqueue(object : Callback<List<Flat>> {
            override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                val flats: List<Flat>? = response.body()
                myFlats = flats as ArrayList<Flat>
                val adapter = RecyclerAdapter(myFlats!!, context!!)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "error :(" + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}