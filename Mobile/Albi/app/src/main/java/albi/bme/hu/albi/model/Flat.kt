package albi.bme.hu.albi.model

import albi.bme.hu.albi.network.ImageDataResponse
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.widget.ImageView


class Flat(var _id: String = "",
           var userID: String = "",
           var price: Int = -1 ,
           var numberOfRooms: Int = -1,
           var description: String = "",
           var email: String = "",
           var phone_number: String = "",
           var address: String = "") {

    var imageData: ArrayList<ImageDataResponse> = ArrayList()
    // https://developer.android.com/guide/topics/resources/drawable-resource
    var image: BitmapDrawable? = null

}

