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

    // TODO DEBUG
    /**
     * az a helyzet, hogy valamiért nem kapjuk meg a házakat amiket elkérünk az onResponse(..)-ban
     * id alapján....ezért null-t kapunk vissza, ami dob egy:
     * kotlin.TypeCastException: null cannot be cast to non-null
     * Request{method=GET, url=http://10.0.2.2:3000/api/user/flats/5bcce492c331c43abc135ac3, tag=null}
     * Response{protocol=http/1.1, code=404, message=Not Found, url=http://10.0.2.2:3000/api/user/flats/5bcce492c331c43abc135ac3}
     * én már kipróbáltam ezer egymillió kis megoldást.....
     */
    private fun networkRequestForMyFlats(recyclerView: RecyclerView) {
        val client = RestApiFactory.createUserClient()
        //val string = user!!._id
        val call = client.getMyFlats("5bcce492c331c43abc135ac3")

        call.enqueue(object : Callback<List<Flat>> {
            override fun onResponse(call: Call<List<Flat>>?, response: Response<List<Flat>>?) {
                if(response?.body().toString() == "OK"){
                    val flats: List<Flat>? = response!!.body()
                    myFlats = flats as ArrayList<Flat>
                    val adapter = RecyclerAdapter(myFlats!!)
                    recyclerView.adapter = adapter
                    Toast.makeText(context, "no error :)", Toast.LENGTH_LONG).show()
                }else{
                    /**
                     * igazából ehhez az id-hez 5bcce492c331c43abc135ac3 van,
                     * csak valamiért null-t kapunk vissza de így legalább nem crashel :D
                     */
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Flat>>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(context, "error :(" + t?.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}