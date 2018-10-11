package albi.bme.hu.albi.network

import albi.bme.hu.albi.interfaces.main.FlatClient
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RestApiFactory {

    companion object {
        val BASE_URL = "http://10.0.2.2:3000"

        fun create(): FlatClient {
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
    }
}

        /**
         * val logging = HttpLoggingInterceptor()
         *    logging.level = HttpLoggingInterceptor.Level.BODY
         *
         *  val httpClient = OkHttpClient.Builder()
         *   httpClient.addInterceptor(logging)
         *
         *  val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
         *  .addConverterFactory(GsonConverterFactory.create())
         *  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
         *  .client(httpClient.build())
         *  .build()
         * return retrofit.create<RestApi>(RestApi::class.java!!)
         *
         */
