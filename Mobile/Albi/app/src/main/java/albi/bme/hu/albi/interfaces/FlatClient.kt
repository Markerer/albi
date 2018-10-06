package albi.bme.hu.albi.interfaces

import albi.bme.hu.albi.model.Flat
import retrofit2.Call
import retrofit2.http.GET

interface FlatClient {

    @GET("/main")
    fun getMainFlats(): Call<List<Flat>>

}