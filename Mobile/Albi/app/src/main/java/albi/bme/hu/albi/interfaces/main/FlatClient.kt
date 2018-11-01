package albi.bme.hu.albi.interfaces.main

import albi.bme.hu.albi.model.Flat
import albi.bme.hu.albi.network.ImageDataResponse
import android.media.Image
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface FlatClient {

    @GET("/api/main/")
    fun getMainFlats(): Call<List<Flat>>

    @GET("/api/main/{_id}")
    fun getMainFlatsByPage(@Path("_id")id: Int): Call<List<Flat>>

    @Multipart
    @POST("flat/upload")
    fun uploadPhoto(@Part photo: MultipartBody.Part) : Call<String>

    @POST("/addflat/{userid}")
    fun uploadFlat(@Path("userid")userId: String,
                   @Body flat: Flat): Call<Flat>

    @GET("/public/uploads/{imageSource}")
    fun getImage(@Path("imageSource") source: String): Call<Image> //lol?

    /**
     * hogyan jutunk egy képhez ?
     * megvan a flatID akkor ---> getImagesIDForFlatID(..)
     * ezzel lekérjük a házhoz tartozó képek ID-jét (imageID)
     * majd az imageID-vel ---> getImageByImageID(..)
     * megkapjuk a kép nevét (és ID-jét amivel hívtuk),
     * és ezzel a névvel a ---> getImageFileByName(..) már megjeleníti
     */

    /**
     * "_id":"5bb9defc3df96e14b077f500",
     * "username":"test",
     * "password":"123456"
     *
     * ehhez az user-hez tartozó flatIDs:
     * "_id":"5bcf6282035f980c4017a59b"
     * "_id":"5bcf6298035f980c4017a59c"
     * "_id":"5bcf62a1035f980c4017a59d"
     * "_id":"5bd09d777ec02a16e899e216"
     */

     /**
     * get images ID
     * http://localhost:3000/flat/images/:flatID (get images id)
      *
      * pl:
      * REQUEST: http://localhost:3000/flat/images/5bca576143bc752f807e9094
      * RESPONSE: [
                     {"_id": "5bdb50b04b1aaa3c94285c92"},
                     {"_id": "5bdb61dec893fe2a00cb4fc6"}, ---> "filename":"image-1541104094255.jpg"
                     {"_id": "5bdb6aa7c893fe2a00cb4fc7"},
                     {"_id": "5bdb6abec893fe2a00cb4fc8"},
                     {"_id": "5bdb6afac893fe2a00cb4fc9"}
                  ]
     */
    @GET("/flat/images/{flatID}")
    fun getImagesIDForFlatID(@Path("flatID") flatID: String): Call<List<String>>

    /**
     * get image name and ID
     * http://localhost:3000/image/5bca57f543bc752f807e9095 (get image name)
     *
     * pl:
     * REQUEST: http://localhost:3000/image/5bdb61dec893fe2a00cb4fc6
     * RESPONSE: {
     *              "_id": "5bdb61dec893fe2a00cb4fc6",
     *               "filename": "image-1541104094255.jpg"
     *            }
     *  azaz visszaadja azt az ID-t amivel hívtuk, illetve az ahhoz
     *  az ID-hez tartozó filename-t
     */
    @GET("/image/{imageID}")
    fun getImageNameByImageID(@Path("imageID") imageID: String): Call<ImageDataResponse>

    /**
     * így már nézhető a kép
     * http://localhost:3000/image-1541104094255.jpg
     */
    @GET("/{filename}")
    fun getImageFileByName(@Path("filename") fileName: String): Call<Image>
}
