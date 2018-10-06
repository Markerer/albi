var mongoose = require('mongoose');

var Schema = mongoose.Schema;


var flatSchema = new Schema({
    flatname: String,
    username: String,
    email: String,
    phone_number: String,
    address: String,
    hasAttachment: Boolean
})
 


var Flats = mongoose.model('Flats', flatSchema);

module.exports = Users;
