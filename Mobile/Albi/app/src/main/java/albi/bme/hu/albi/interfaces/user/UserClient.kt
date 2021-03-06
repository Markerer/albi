package albi.bme.hu.albi.interfaces.user

import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import albi.bme.hu.albi.network.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface UserClient {

    @POST("/api/user/")
    fun createUser(@Body user: User): Call<User>

    @PUT("/api/user/")
    fun updateUser(@Body user: User, @Header("Authorization") token: String ): Call<String>

    @POST("/api/login/")
    fun loginUser(@Body user: User): Call<LoginResponse>

    @GET("/api/users/{username}")
    fun getUserByUserName(@Path("username") username : String ): Call<User>

    @GET("/user/flats/{userID}")
    fun getMyFlats(@Path("userID")userId: String): Call<List<Flat>>

}