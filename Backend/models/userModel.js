var mongoose = require('mongoose');

var Schema = mongoose.Schema;


var userSchema = new Schema({
    username: String,
    email: String,
    phone_number: String,
    address: String
})
 


var Users = mongoose.model('Users', userSchema);

module.exports = Users;
