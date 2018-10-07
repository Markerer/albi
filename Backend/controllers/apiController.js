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

    //list all users
    app.get('/api/users', function(req, res){
        Users.find({}, function(err, users) {
           
            res.send(users);  
          });
    });
    
    //list all flats
    app.get('/api/main/', function(req, res){
        Flats.find({}, function(err, flats){
            res.send(flats);
        })
    });
    
    app.post('/api/login', function(req, res){
        
        Users.find({ username: req.params.username, password: req.params.password }, function(err, users) {
            if (err) throw err;
            if(users.password === req.params.password)
            res.send("OK");
            else{
                res.send("NOT OK");
            }
        });

        
    })
    
    app.get('/api/user/:id', function(req, res) {
       
       Users.findById({ _id: req.params.id }, function(err, user) {
           if (err) throw err;
           
           res.send(user);
       });
        
    });

    
    //user updater, create user
    app.post('/api/user', function(req, res) {
        
        if (req.body.id) {
            
            Users.findByIdAndUpdate(req.body.id, {username: req.body.username, password: req.body.password, email: req.body.email, phone_number: req.body.phone_number, address: req.body.address }, function(err, user){
                if (err) throw err;
                
                res.send('Success');
            });
                
           
        }
        
        else {
           
        
           var newUser = Users({
               username: req.body.username,
               password: req.body.password,
               email: req.body.email,
               phone_number: req.body.phone_number,
               address: req.body.address
           });
           
           Users.create(newUser, function(err, results){
                if(err) throw err;
            
                res.send(results);
                
           });

            
        }
        
    });

    //update, create flats
    app.post('/api/main', function(req, res) {
        
        if (req.body.id) {
            
            Flats.findByIdAndUpdate(req.body.id, {flatname: req.body.flatname, username: req.body.username, email: req.body.email, phone_number: req.body.phone_number, address: req.body.address, hasAttachment: req.body.hasAttachment }, function(err, user){
                if (err) throw err;
                
                res.send('Success');
            });
                
           
        }
        
        else {
           
        
           var newFlat = Flats({
            flatname: req.body.flatname,
            username: req.body.username,
            email: req.body.email,
            phone_number: req.body.email,
            address: req.body.address,
            hasAttachment: req.body.hasAttachment
           });
           
           Flats.create(newFlat, function(err, results){
                if(err) throw err;
            
                res.send(results);
                
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