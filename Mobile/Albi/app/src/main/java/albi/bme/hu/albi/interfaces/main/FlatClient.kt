package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.model.Flat
import android.media.Image
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

    @POST("/addflat/{userid}")
    fun uploadFlat(@Path("userid")userId: String,
                   @Body flat: Flat): Call<Flat>

    @GET("/public/uploads/{imageSource}")
    fun getImage(@Path("imageSource") source: String): Call<Image> //lol?
}
