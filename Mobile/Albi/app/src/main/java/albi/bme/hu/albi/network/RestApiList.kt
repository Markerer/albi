package albi.bme.hu.albi.network

import albi.bme.hu.albi.model.Flat
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestApiList(private val listener: ListInterface) {
    interface ListInterface {
        fun photoLoaded(flat: Flat)
    }



    fun networkRequestForImagesIDs(flat: Flat) {
        val client = RestApiFactory.createFlatClient()
        val call = client.getImagesIDForFlatID(flat._id)


        call.enqueue(object : Callback<List<ImageDataResponse>> {
            override fun onResponse(call: Call<List<ImageDataResponse>>, response: Response<List<ImageDataResponse>>) {
                val actualFlatImageData = response.body() as ArrayList<ImageDataResponse>

                if (actualFlatImageData.size != 0) {
                    for (j in actualFlatImageData.indices) {
                        flat.imageNames.add(actualFlatImageData[j].filename)
                    }
                    actualFlatImageData.clear()
                }
                listener.photoLoaded(flat)
            }

            override fun onFailure(call: Call<List<ImageDataResponse>>, t: Throwable?) {
                t?.printStackTrace()
            }
        })
    }
}