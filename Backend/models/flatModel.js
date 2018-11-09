var mongoose = require('mongoose');
var mongoosePaginate = require('mongoose-paginate');

var Schema = mongoose.Schema;


var flatSchema = new Schema({
    userID: String,
    price: String,
    numberOfRooms: String,
    description: String,
    email: String,
    phone_number: String,
    zipCode: String,
    city: String,
    address: String,
    forSale: Boolean,
});
flatSchema.plugin(mongoosePaginate);
 


var Flats = mongoose.model('Flats', flatSchema);

module.exports = Flats;
