var Users = require('../models/userModel');

module.exports = function(app) {
    
   app.get('/api/setupUsers', function(req, res) {
       
       // seed database   
       var starterUsers = [
           {
               username: 'admin',
               email: 'admin@gmail.com',
               phone_number: '0000000',
               adress: 'asd utca'
           }
       ];
       
       
       Users.create(starterUsers, function(err, results){
           res.send(results);
       });
       
   });
    
}