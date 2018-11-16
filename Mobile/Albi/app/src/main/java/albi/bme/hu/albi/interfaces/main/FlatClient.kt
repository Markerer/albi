package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.FlatPageResponse
import albi.bme.hu.albi.network.ImageDataResponse
import albi.bme.hu.albi.network.ImageUploadResponse
import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FlatClient {

    @GET("/api/main/")
    fun getMainFlats(): Call<List<Flat>>

    @GET("/api/main/{page}")
    fun getMainFlatsByPage(@Path("page")page: Int): Call<FlatPageResponse>

    @Multipart
    @POST("flat/upload/{flatID}")
    fun uploadPhoto(@Path("flatID") flatID: String,
                    @Part photo: MultipartBody.Part) : Call<ResponseBody>

    @POST("/addflat/{userid}")
    fun uploadFlat(@Path("userid")userId: String,
                   @Body flat: Flat): Call<Flat>

    @GET("/flat/images/{flatID}")
    fun getImagesIDForFlatID(@Path("flatID") flatID: String): Call<List<ImageDataResponse>>

    @PUT("/flats")
    fun updateFlat(@Body flat: Flat): Call<String>

    /**
     * @param pageID
     * mindenféleképpen kell, enélkül hibát dob
     *
     * @param MaxPrice
     * @param numberOfRooms
     * @param address
     * Valamelyiknek mindenféleképpen benne kell lennie
     * a kérésben, különben nem lesz jó.
     * csak a szobaszám nem lehet egyedül
     * egyenként is lekérdezhetőek, ekkor a többi üres marad
     * illetve párokban is
     * pl ez valid: http://localhost:3000/flats/1/25000?numberOfRooms=10&address=
     * ez invalid: http://localhost:3000/flats/1/?numberOfRooms=3&address=
     *{MaxPrice}?numberOfRooms={numberOfRooms}&address={address}
     */
    @GET("/flats/{pageID}/{MaxPrice}?")
    fun getFlatsBySearch(@Path("pageID") pageID: Int,
                         @Path("MaxPrice") MaxPrice: Int,
                         @Query("numberOfRooms") numberOfRooms: Int,
                         @Query("address") address: String): Call<FlatPageResponse>

}
