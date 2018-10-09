package albi.bme.hu.albi.fragments.fragments.mainview

import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.interfaces.main.FlatClient
import albi.bme.hu.albi.model.Flat
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HouseDetailFragment : Fragment(){

    var usersData = ArrayList<Flat>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initializationRecycle()
    }

    private fun initializationRecycle(): RecyclerView? {
        val recyclerView = RecyclerView(context!!)
        recyclerView.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)

        val builder: Retrofit.Builder = Retrofit.Builder()
                //https://stackoverflow.com/questions/40077927/simple-retrofit2-request-to-a-localhost-server
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()
        val client: FlatClient = retrofit.create(FlatClient::class.java)
        val call = client.getMainFlats()


        call.enqueue(object : Callback<List<Flat>> {
            override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                val flats: List<Flat>? = response.body()
                usersData = flats as ArrayList<Flat>
                val adapter = RecyclerAdapter(usersData)
                recyclerView.adapter = adapter
                Toast.makeText(activity, "no error :)", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(activity, "error :("+t.message, Toast.LENGTH_LONG).show()
            }
        })
/*
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
*/
        return recyclerView
    }


    public fun setFlatsData(listOfFlats: List<Flat>){
        this.usersData = ArrayList(listOfFlats)
    }



}