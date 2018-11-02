package albi.bme.hu.albi.fragments.fragments.mainview

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.interfaces.main.FlatClient
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.ImageDataResponse
import albi.bme.hu.albi.network.RestApiFactory
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.os.Handler
import android.os.StrictMode
import com.google.gson.internal.bind.TypeAdapters.URL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.house_detail_fragment_row.*
import java.io.IOException
import java.io.InputStream
import java.net.URL

class HouseDetailFragment : Fragment() {

    var usersData = ArrayList<Flat>()
    private var flatsImageIDs: ArrayList<String>? = ArrayList()
    private var actualImageData: ImageDataResponse? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.house_detail_fragment, container, false)

        recyclerView = view.findViewById<RecyclerView>(R.id.rvHouseDetail)

        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            networkRequestForMainFlats()
            val handler = Handler()
            handler.postDelayed({ swipeContainer.isRefreshing = false }, 1000)
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_green_light)

        initializationRecycle()

        return view
    }

    private fun initializationRecycle() {
        recyclerView?.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
        /**
         * szerintem ide kellene meghívni a többi hívást, előbb leszedni a képeket
         * hogy a recycelView adapterének már a képekkel feltöltött házak menjenek
         * viszont akkor meg nem lesz "_id"-nk.....
         *
         * szóval lehet át kellene tenni a:
         * val adapter = RecyclerAdapter(usersData)
         *  recyclerView.adapter = adapter
         *
         *  részt a network hívások után, h már minden adattal hívódjon meg
         */
       // networkRequestForImagesIDs(usersData.get(0)._id) // 5bca576143bc752f807e9094
       // networkRequestForImageName(flatsImageIDs?.get(0)!!)
        val requestNameID:String = "image-1541115065336.jpeg"
        //networkRequestForImageView(requestNameID)
        networkRequestForMainFlats()
    }

    // networkRequestForMainFlats(recyclerView: RecyclerView)
    private fun networkRequestForMainFlats() {
        val client = RestApiFactory.createFlatClient()
        val call = client.getMainFlats()

        call.enqueue(object : Callback<List<Flat>> {
            override fun onResponse(call: Call<List<Flat>>, response: Response<List<Flat>>) {
                val flats: List<Flat>? = response.body()
                usersData = flats as ArrayList<Flat>
                //val adapter = RecyclerAdapter(usersData)
                //recyclerView.adapter = adapter
                // TODO PICASSO --> http://square.github.io/picasso/
                for (i in usersData.indices){
                    usersData[i].imageURL = "https://www.gdn-ingatlan.hu/nagy_kep/one/gdn-ingatlan-243479-1535708208.53-watermark.jpg" //http://localhost:3000/image-1541115065336.jpeg
                }
                val adapter = RecyclerAdapter(usersData)
                recyclerView?.adapter = adapter
                Toast.makeText(activity, "no error :)", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<List<Flat>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForMainFlats()" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun networkRequestForImagesIDs(flatID: String) {
        val client = RestApiFactory.createFlatClient()
        val call = client.getImagesIDForFlatID(flatID)

        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>?, response: Response<List<String>>?) {
                val imageIDs: List<String>? = response?.body()
                flatsImageIDs = imageIDs as? ArrayList<String>
                Toast.makeText(activity, "succesfully get Image IDs!! :)", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<List<String>>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForImagesIDs()" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun networkRequestForImageName(imageID: String) {
        val client = RestApiFactory.createFlatClient()
        val call = client.getImageNameByImageID(imageID)

        call.enqueue(object : Callback<ImageDataResponse> {
            override fun onResponse(call: Call<ImageDataResponse>?, response: Response<ImageDataResponse>?) {
                val imageNames: ImageDataResponse? = response?.body()
                actualImageData = imageNames
                Toast.makeText(activity, "succesfully get Image Datas!! :)", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<ImageDataResponse>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForImageName()" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun networkRequestForImageView(imagename: String) {
        val client = RestApiFactory.createFlatClient()
        val call = client.getImageFileByName(imagename)

        call.enqueue(object : Callback<Image> {
            override fun onResponse(call: Call<Image>?, response: Response<Image>?) {
                val image: Image? = response?.body()
                // PROBA, mindegyiknek ugyan azt, megnézni,
                // hogy legalább működik e

                for (i in usersData.indices){
                    usersData[i].image = image
                }

                // TODO!!!!!! https://stackoverflow.com/questions/41311179/how-do-i-set-an-image-in-recyclerview-in-a-fragment-from-the-drawable-folder
                /**
                 * bekellene állítani a recycleViewbem az adott sor image-ját
                 * lehet kell a flatba egy megfelelő attribútum...
                 */

            }

            override fun onFailure(call: Call<Image>?, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForImageView()" + t?.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    // https://stackoverflow.com/questions/45830529/how-to-convert-image-url-into-drawable-int
    // https://www.youtube.com/watch?v=japhFMXAJZw
    private fun convertServerImageURLintoBitmap(){
        Picasso.get().load("http://localhost:3000/image-1541115065336.jpeg").into(ivFlatImagePreview)
    }


}

