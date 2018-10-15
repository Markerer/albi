package albi.bme.hu.albi.network

import albi.bme.hu.albi.interfaces.main.FlatClient
import albi.bme.hu.albi.interfaces.user.UserClient
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RestApiFactory {

    companion object {
        val BASE_URL = "http://10.0.2.2:3000"

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
