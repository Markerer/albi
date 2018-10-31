package albi.bme.hu.albi.interfaces.user

import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserClient {

    @POST("/api/user/")
    fun createUser(@Body user: User): Call<String>

    @POST("/api/user/")
    fun updateUser(@Body user: User): Call<String>

    /**
     * Send: JSON
     * Get: String
     */
    @POST("/api/login/")
    fun loginUser(@Body user: User): Call<String>

    /**
     * Send: String (the username)
     *  GET: User (Every detail of the user)
     */
    @GET("/api/users/{username}")
    fun getUserByUserName(@Path("username") username : String ): Call<User>

    @GET("/api/user/{_id}")
    fun getUserById(@Path("_id") id : String): Call<User>

    @GET("/api/user/flats/{userID}")
    fun getMyFlats(@Path("userID")userId: String): Call<List<Flat>>

}