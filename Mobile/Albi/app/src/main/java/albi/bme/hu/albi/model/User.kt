package albi.bme.hu.albi.model

class User {

    private var id: Integer? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var phone_number: String = ""

    constructor(userName: String, emailAddress: String, password: String, phoneNumber: String = "") {
        this.username = userName
        this.email = emailAddress
        this.password = password
        this.phone_number = phoneNumber
    }

    constructor(userName: String, password: String){
        this.username = userName
        this.password = password
    }

   public fun getId(): Integer? = this.id
}