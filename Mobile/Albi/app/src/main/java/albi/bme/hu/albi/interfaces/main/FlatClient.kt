package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.model.Flat
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface FlatClient {

    @GET("/api/main/")
    fun getMainFlats(): Call<List<Flat>>

    @GET("/api/main/{_id}")
    fun getMainFlatsByPage(@Path("_id")id: Int): Call<List<Flat>>

    @Multipart
    @POST("flat/upload")
    fun uploadPhoto(@Part photo: MultipartBody.Part) : Call<String>

    @POST("/addflat/")
    fun uploadFlat(price: Int,
                   numberOfRooms: Int,
                   description : String,
                   email: String,
                   phone_number: String,
                   address: String)
}
