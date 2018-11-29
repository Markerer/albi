package albi.bme.hu.albi.network

import albi.bme.hu.albi.interfaces.main.FlatClient
import albi.bme.hu.albi.interfaces.user.UserClient
import albi.bme.hu.albi.model.Flat
import android.support.v7.widget.RecyclerView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RestApiFactory {

    companion object {
        /**
         * emulator localhost: http://10.0.2.2:3000/
         * online: https://albinodejs.azurewebsites.net
         */
        val BASE_URL = "https://albinodejs.azurewebsites.net/"

        fun createFlatClient(): FlatClient {
            val gson = GsonBuilder()
                    .setLenient()
                    .setPrettyPrinting()
                    .create()

            val builder = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))

            val retrofit = builder.build()
            return retrofit.create(FlatClient::class.java)
        }

        fun createFlatClientPhoto(): FlatClient {
            val gson = GsonBuilder()
                    .create()

            val builder = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))

            val retrofit = builder.build()
            return retrofit.create(FlatClient::class.java)
        }

        fun createUserClient(): UserClient {
            val gson = GsonBuilder()
                    .setLenient()
                    .setPrettyPrinting()
                    .create()

            val builder = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))

            val retrofit = builder.build()
            return retrofit.create(UserClient::class.java)
        }

    }
}
