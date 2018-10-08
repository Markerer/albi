package albi.bme.hu.albi.interfaces.user

import albi.bme.hu.albi.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserClient {

    @POST("/user/")
    fun createNewUser(@Body user: User): Call<User>

    /**
     * JSon-t küldünk
     * Stringet kapunk vissza: OK -- NOT OK
     */
    @POST("/api/login/")
    fun loginUser(@Body user: User): Call<String>

}