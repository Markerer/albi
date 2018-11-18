package albi.bme.hu.albi.fragments.profile

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.ImageDataResponse
import albi.bme.hu.albi.network.RestApiFactory
import albi.bme.hu.albi.network.RestApiList
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

class MyAdvertisementsFragment : Fragment(), RestApiList.ListInterface {


    var myFlats: ArrayList<Flat> = ArrayList()
    var user: User? = null
    lateinit var recyclerView: RecyclerView

    private val restApiList = RestApiList(this)
    override fun photoLoaded(flat: Flat) {
        recyclerView.adapter?.notifyItemChanged(myFlats.indexOf(flat))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_house_advertisements_fragment, container, false)
        recyclerView = view.findViewById(R.id.rvMyHouses)

        initializationRecycle()

        return view
    }

    private fun initializationRecycle() {
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
        networkRequestForMyFlats(recyclerView)
    }

    private fun networkRequestForMyFlats(recyclerView: RecyclerView) {
        if(user != null){

            val client = RestApiFactory.createUserClient()
            val call = client.getMyFlats(user!!._id!!)

            call.enqueue(object : Callback<List<Flat>> {
                override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                    val flats: List<Flat>? = response.body()
                    myFlats = flats as ArrayList<Flat>

                    for (i in myFlats.indices) {
                        restApiList.networkRequestForImagesIDs(myFlats[i])
                    }

                    val adapter = RecyclerAdapter(myFlats, context!!, owner = true)
                    recyclerView.adapter = adapter
                }

                override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(context, "error :(" + t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

}