var mongoose = require('mongoose');
var mongoosePaginate = require('mongoose-paginate');

var Schema = mongoose.Schema;


var dateSchema = new Schema({
    flatID: String,
    counter: Number,
    date: String 
});

dateSchema.plugin(mongoosePaginate); 


var Dates = mongoose.model('Dates', dateSchema);

module.exports = Dates;
