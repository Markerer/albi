var mongoose = require('mongoose');
var mongoosePaginate = require('mongoose-paginate');
const bcrypt = require("bcrypt");
var Schema = mongoose.Schema;


var userSchema = new Schema({
    username: String,
    password: String,
    email: String,
    phone_number: String,
    address: String
});

userSchema.plugin(mongoosePaginate); 


var Users = mongoose.model('Users', userSchema);

module.exports = Users;
