package albi.bme.hu.albi.model

import java.io.Serializable

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
           var forSale: Boolean = false) : Serializable{

    var imageNames: ArrayList<String>? = ArrayList()

}

