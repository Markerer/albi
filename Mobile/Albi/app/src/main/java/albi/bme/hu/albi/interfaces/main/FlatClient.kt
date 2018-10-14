package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.model.Flat
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FlatClient {

    @GET("/api/main/")
    fun getMainFlats(): Call<List<Flat>>

    @GET("/api/main/{_id}")
    fun getMainFlatsByPage(@Path("_id")id: Int): Call<List<Flat>>


}
