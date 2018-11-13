package albi.bme.hu.albi.fragments.profile

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.ImageDataResponse
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

    var myFlats: ArrayList<Flat> = ArrayList()
    var user: User? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_house_advertisements_fragment, container, false)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvMyHouses)

        initializationRecycle()

        return view
    }

    private fun initializationRecycle() {
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

                for (i in myFlats.indices) {
                    networkRequestForImagesIDs(myFlats[i])
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
                recyclerView.adapter?.notifyItemChanged(myFlats.indexOf(flat))
            }

            override fun onFailure(call: Call<List<ImageDataResponse>>, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForImagesIDs()" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}