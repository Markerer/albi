package albi.bme.hu.albi.model

class User {

    private var id: Integer? = null
    var username: String = ""
    var emailAddress: String = ""
    var password: String = ""
    var phoneNumber: String = ""

    constructor(userName: String = "", emailAddress: String, password: String, phoneNumber: String = "") {
        this.username = userName
      //  this.emailAddress = emailAddress
        this.password = password
        //this.phoneNumber = phoneNumber
    }

    constructor(userName: String, password: String){
        this.username = userName
        this.password = password
    }

   public fun getId(): Integer? = this.id
}