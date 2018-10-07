var Users = require('../models/userModel');
var Flats = require('../models/flatModel');

module.exports = function(app) {
    
   app.get('/api/setupUsers', function(req, res) {
       
       // seed database   
       var starterUsers = [
           {
               username: 'admin',
               email: 'admin@gmail.com',
               phone_number: '000000',
               address: 'asd utca'
           }
       ];

       Users.create(starterUsers, function(err, results){
           res.send(results);
       });
       
   });
    
   app.get('/api/setupFlats', function(req, res){

        //seed database
        var starterFlats = [
            {
                flatname: 'Test',
                username: 'admin',
                email: 'admin@gmail.com',
                phone_number: '000000',
                address: 'asd utca',
                hasAttachment: Boolean
            }
        ];

        Flats.create(starterFlats, function(err, result){
             res.send(result);
         });

   });
}