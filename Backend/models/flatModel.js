var mongoose = require('mongoose');
var mongoosePaginate = require('mongoose-paginate');

var Schema = mongoose.Schema;


var flatSchema = new Schema({
    flatname: String,
    username: String,
    email: String,
    phone_number: String,
    address: String,
    hasAttachment: Boolean
});
flatSchema.plugin(mongoosePaginate);
 


var Flats = mongoose.model('Flats', flatSchema);

module.exports = Flats;
