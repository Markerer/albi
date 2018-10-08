package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.model.Flat
import retrofit2.Call
import retrofit2.http.GET

interface FlatClient {

    @GET("/api/main/")
    fun getMainFlats(): Call<List<Flat>>

}
