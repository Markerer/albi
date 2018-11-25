package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.helpers.Today
import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.FlatDateResponse
import albi.bme.hu.albi.network.FlatPageResponse
import albi.bme.hu.albi.network.ImageDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FlatClient {

    @GET("/api/main/")
    fun getMainFlats(): Call<List<Flat>>

    @GET("/api/main/{page}")
    fun getMainFlatsByPage(@Path("page") page: Int): Call<FlatPageResponse>

    @Multipart
    @POST("/flat/upload/{flatID}")
    fun uploadPhoto(@Path("flatID") flatID: String,
                    @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
                    @Header("Authorization") token: String): Call<ResponseBody>

    @POST("/addflat/{userid}")
    fun uploadFlat(@Path("userid") userId: String,
                   @Body flat: Flat,
                   @Header("Authorization") token: String): Call<Flat>

    @GET("/flat/images/{flatID}")
    fun getImagesIDForFlatID(@Path("flatID") flatID: String): Call<List<ImageDataResponse>>

    @PUT("/flats")
    fun updateFlat(@Body flat: Flat,
                   @Header("Authorization") token: String): Call<String>

    @POST("/date/{flatID}")
    fun addView(@Path("flatID") flatID: String,
                @Body date: Today,
                @Header("Authorization") token: String): Call<ResponseBody>

    @GET("/flats/{pageID}/{MaxPrice}?")
    fun getFlatsBySearch(@Path("pageID") pageID: Int,
                         @Path("MaxPrice") MaxPrice: Int,
                         @Query("numberOfRooms") numberOfRooms: Int?,
                         @Query("address") address: String?): Call<FlatPageResponse>

    @GET("/flat/dates/{flatID}")
    fun getAllDatesForFlat(@Path("flatID") flatID: String): Call<List<FlatDateResponse>>

}
