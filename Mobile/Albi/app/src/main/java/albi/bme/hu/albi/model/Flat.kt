package albi.bme.hu.albi.model

import albi.bme.hu.albi.network.ImageDataResponse
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.widget.ImageView


class Flat(var _id: String = "",
           var userID: String = "",
           var price: String = "" ,
           var numberOfRooms: String = "",
           var description: String = "",
           var email: String = "",
           var phone_number: String = "",
           var zipCode: String = "",
           var city: String = "",
           var address: String = "",
           var forSale: Boolean = false) {

    var imageData: ArrayList<ImageDataResponse> = ArrayList()
    // https://developer.android.com/guide/topics/resources/drawable-resource
    var image: Image? = null
    //https://stackoverflow.com/questions/8937036/what-is-the-difference-between-bitmap-and-drawable-in-android
    //https://corochann.com/convert-between-bitmap-and-drawable-313.html
    var bitmapImage: Bitmap? = null // BitmapDrawable ???

    var imageURLs: ArrayList<String>? = ArrayList()
    var imageIDs: ArrayList<String>? = ArrayList()
    var imageNames: ArrayList<String>? = ArrayList()

}

