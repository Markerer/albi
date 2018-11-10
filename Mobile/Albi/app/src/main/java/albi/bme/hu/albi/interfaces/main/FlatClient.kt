package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.FlatPageResponse
import albi.bme.hu.albi.network.ImageDataResponse
import albi.bme.hu.albi.network.ImageUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface FlatClient {

    @GET("/api/main/")
    fun getMainFlats(): Call<List<Flat>>

    @GET("/api/main/{page}")
    fun getMainFlatsByPage(@Path("page")page: Int): Call<FlatPageResponse>

    @Multipart
    @POST("flat/upload/{flatID}")
    fun uploadPhoto(@Path("flatID") flatID: String, @Part photo: MultipartBody.Part) : Call<ImageUploadResponse>

    @POST("/addflat/{userid}")
    fun uploadFlat(@Path("userid")userId: String,
                   @Body flat: Flat): Call<Flat>


    @GET("/flat/images/{flatID}")
    fun getImagesIDForFlatID(@Path("flatID") flatID: String): Call<List<ImageDataResponse>>

}
