var mongoose = require('mongoose');
var mongoosePaginate = require('mongoose-paginate');

var Schema = mongoose.Schema;


var imageSchema = new Schema({
    flatID: String,
    fieldname: String,
    originalname: String,
    encoding: String,
    mimetype: String,
    destination: String,
    filename: String,
    path: String,
    size: Number
  
});

imageSchema.plugin(mongoosePaginate); 


var Images = mongoose.model('Images', imageSchema);

module.exports = Images;
