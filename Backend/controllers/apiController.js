var Users = require('../models/userModel');
var Flats = require('../models/flatModel');
var bodyParser = require('body-parser');

module.exports = function(app) {
    
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: true }));
    
    app.get('/api/users/:uname', function(req, res) {
        
        Users.find({ username: req.params.uname }, function(err, users) {
            if (err) throw err;
            
            res.send(users);
        });
        
    });
    
    app.get('/api/user/:id', function(req, res) {
       
       Users.findById({ _id: req.params.id }, function(err, user) {
           if (err) throw err;
           
           res.send(user);
       });
        
    });
    
    app.post('/api/user', function(req, res) {
        
        if (req.body.id) {
            
            Users.findByIdAndUpdate(req.body.id, {user: req.body.username, email: req.body.email, phone_number: req.body.phone_number, address: req.body.address }, function(err, user){
                if (err) throw err;
                
                res.send('Success');
            });
                
           
        }
        
        else {
           
        
           var newUser = Users({
               username: req.body.username,
               email: req.body.email,
               phone_number: req.body.phone_number,
               address: req.body.address
           });
           
           Users.create(newUser, function(err, results){
            if(err) throw err;
                res.send(results, 'Success');
                
           });

            
        }
        
    });
    
    app.delete('/api/user', function(req, res) {
        
        Users.findByIdAndRemove(req.body.id, function(err) {
            if (err) throw err;
            res.send('Success');
        })
        
    });
    
}