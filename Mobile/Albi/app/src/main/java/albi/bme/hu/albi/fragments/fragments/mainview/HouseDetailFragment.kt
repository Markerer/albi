package albi.bme.hu.albi.fragments.fragments.mainview

import albi.bme.hu.albi.R
import albi.bme.hu.albi.adapter.recycleviewadapter.RecyclerAdapter
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.ImageDataResponse
import albi.bme.hu.albi.network.RestApiFactory
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
import android.os.Handler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.house_detail_fragment_row.*

class HouseDetailFragment : Fragment() {

    var usersData = ArrayList<Flat>()
    // egy db flat-hez tartozó ID-k, mindig az aktuális
    private var actualImageData: ImageDataResponse? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.house_detail_fragment, container, false)

        recyclerView = view.findViewById(R.id.rvHouseDetail)

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
        //Thread(Runnable { networkRequestForMainFlats() }).start()
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


                // TODO PICASSO --> http://square.github.io/picasso/
                /**
                 * flatID --> imageID --> imageName
                 */
                for (i in usersData.indices){
                    /**
                     * nevet és id-t is visszaadja egy képhez
                     * "_id": "5bdb61dec893fe2a00cb4fc6",
                     * "filename": "image-1541104094255.jpg"
                     */
                    networkRequestForImagesIDs(usersData[i])
                    //Toast.makeText(activity, "actualFlatImageData.filename: " + actualFlatImageData?.get(0)!!.filename, Toast.LENGTH_LONG).show()
                    // TODO: Error Array "java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0"
                    /**
                     * ezzel error: actualFlatImageData?.get(0)!!.filename
                     * így jó:.... image-1541115065336.jpeg
                     */

                    //"https://www.gdn-ingatlan.hu/nagy_kep/one/gdn-ingatlan-243479-1535708208.53-watermark.jpg"
                    // http://localhost:3000/image-1541115065336.jpeg
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

    private fun networkRequestForImagesIDs(flat: Flat) {
        val client = RestApiFactory.createFlatClient()
        val call = client.getImagesIDForFlatID(flat._id)

        call.enqueue(object : Callback<List<ImageDataResponse>> {
            override fun onResponse(call: Call<List<ImageDataResponse>>, response: Response<List<ImageDataResponse>>) {
                val imageIDs: List<ImageDataResponse>? = response.body()
                val actualFlatImageData = imageIDs as ArrayList<ImageDataResponse>

                if(actualFlatImageData.size != 0) {
                    for (j in actualFlatImageData.indices) {
                        flat.imageNames!!.add(actualFlatImageData[j].filename) //actualFlatImageData?.get(i)!!.filename "image-1541115065336.jpeg"
                    }
                    actualFlatImageData.clear()
                }
                //Toast.makeText(activity, "succesfully get Image IDs!! :)", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<List<ImageDataResponse>>, t: Throwable?) {
                t?.printStackTrace()
                Toast.makeText(activity, "error in: networkRequestForImagesIDs()" + t?.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}

