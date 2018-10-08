package albi.bme.hu.albi.model

class User {

    private var id: Integer? = null
    var name: String = ""
    var emailAddress: String = ""
    var password: String = ""
    var phoneNumber: String = ""

    constructor(name: String, emailAddress: String, password: String, phoneNumber: String) {
        this.name = name
        this.emailAddress = emailAddress
        this.password = password
        this.phoneNumber = phoneNumber
    }

    public fun getId(): Integer? = this.id
}