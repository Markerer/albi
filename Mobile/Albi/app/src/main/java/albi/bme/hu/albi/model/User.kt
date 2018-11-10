package albi.bme.hu.albi.model

import java.io.Serializable

class User(var username: String, var email: String, var password: String, var phone_number: String = "", var address: String = ""): Serializable {

    var _id: String? = null

    constructor(username: String, password: String) : this(
            username,
            "",
            password
    )
}