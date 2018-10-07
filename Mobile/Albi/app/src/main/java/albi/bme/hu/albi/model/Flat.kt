package albi.bme.hu.albi.model

class Flat {
    var price: Int = 0
    var address: String = ""
    var numberOfBeds: Int = 0

    constructor(price: Int = 0, address: String = "", numberOfBeds: Int = 0){
        this.price = price
        this.address = address
        this.numberOfBeds = numberOfBeds
    }
}