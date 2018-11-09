var Users = require('../models/userModel');
var Flats = require('../models/flatModel');
var Images = require('../models/image');
var bodyParser = require('body-parser');


module.exports = function(app) {
    
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: true }));
    
    

    //list all users
    app.get('/api/users', function(req, res){
        Users.find({}, function(err, users) {
           if(err){
               res.send("There are no users");
           }
            res.send(users);  
          });
    });
    
    //list all flats
    app.get('/api/main/', function(req, res){
        Flats.find({}, function(err, flats){
            res.send(flats);
        })
    });

    //update flats
    app.put('/flats', function(req, res) {
                
        Flats.findByIdAndUpdate(req.body._id, {userID: req.body.userID,
            price: req.body.price,
            numberOfRooms: req.body.numberOfRooms,
            description: req.body.description,
            email: req.body.email,
            phone_number: req.body.phone_number,
            zipCode: req.body.zipCode,
            city: req.body.city,
            address: req.body.address,
            forSale: req.body.forSale}, function(err, user){
            if (err) throw err;
            
            res.send('Success');
        });
            
       
    });

    //paging 10 record/page
    app.get('/api/main/:pageid', function(req, res){
        var pageid = req.params.pageid;
        
        Flats.paginate({}, { page: pageid, limit: 10 }, function(err, result) {
            // result.docs
            // result.total
            // result.limit - 10
            // result.offset - 20
            if(pageid > 0 && result.pages >= pageid){
                res.send(result);
            }else{
                res.send("Invalid page");
            }
            
          });
         
    });
    
    //login: check password and username
    app.post('/api/login', function(req, res){
        
        var user = new Users({
            username: req.body.username,
            password: req.body.password
          })

        Users.findOne({ username: user.username }, function(err, tempUser) {
            if (err) throw err;
            if(tempUser && user.password === tempUser.password){
                
            res.send("OK");
            }
            else{
                res.send("NOT OK");
            }
        });

        
    })
    
    //BEGIN Upload Image
    const multer  = require('multer')
    const path = require('path');

    const storage = multer.diskStorage({
        destination: './public/uploads/',
        filename: function(req, file, cb){
          cb(null,file.fieldname + '-' + Date.now() + path.extname(file.originalname));
        }
      });

    // Init upload
    const upload = multer({
        storage: storage,
        limits:{fileSize: 1000000},
        fileFilter: function(req, file, cb){
          checkFileType(file, cb);
        }
    }).single('image');

    //Check File Type
    function checkFileType(file, cb){
        // Allowed ext
        const filetypes = /jpeg|jpg|png/;
        // Check ext
        const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
        // Check mime
        const mimetype = filetypes.test(file.mimetype);
      
        if(mimetype && extname){
          return cb(null,true);
        } else {
          cb('Error: Images Only!');
        }
      }
    
    //Upload image
    app.post('/flat/upload/:id', function(req, res){
        upload(req, res, (err) => {
          if(err){
            res.send('err');
          } else {
            if(req.file == undefined){
              res.send('Error: No File Selected!')
            } else {
                
                var newImage = Images({
                    flatID: req.params.id,
                    fieldname: req.file.fieldname,
                    originalname: req.file.originalname,
                    encoding: req.file.encoding,
                    mimetype: req.file.mimetype,
                    destination: req.file.destination,
                    filename: req.file.filename,
                    path: req.file.path,
                    size: req.file.size
                });

                Images.create(newImage, function(err, results){
                    if(err) throw err;
                    res.send(results);
                });

            }
          }
        });
      });
    
    //END of upload Image

    //Load Image
    app.get('/flat/images/:flatId', function(req, res){
        Images.find({flatID: req.params.flatId }, function(err, images){
            if(err) throw err;
            res.send(images);
        }).select('_id filename');
    });

    app.get('/image/:imageID', function(req, res){
        Images.findById({ _id: req.params.imageID }, function(err, image) {
            if(err){
                res.send("There is no image with this ID");
            }
               
               res.send(image);
           }).select('filename');

    });


    //delete image

    app.delete('/image/:imageID', function(req, res) {
        
        Images.findByIdAndRemove(req.params.imageID, function(err) {
            if (err){
                res.send('Unsuccessful');
            }
            
            res.send('Success');
        })
        
    });

    app.delete('/flat/:flatID', function(req, res) {
        
        Flats.findByIdAndRemove(req.params.flatID, function(err) {
            if (err){
                res.send('Unsuccessful');
            }
            
            res.send('Success');
        })
        
    });

    //search by username
    app.get('/api/users/:uname', function(req, res) {
        
        Users.findOne({ username: req.params.uname }, function(err, user) {
            if(err){
                res.send("There is no user with this name");
            }
            
            res.send(user);
        });
        
    });

    //search user by id
    app.get('/api/user/:id', function(req, res) {
       
       Users.findById({ _id: req.params.id }, function(err, user) {
        if(err){
            res.send("There is no user with this ID");
        }
           
           res.send(user);
       });
        
    });
 


    
    //user updater, create user
    app.put('/api/user', function(req, res) {
                
            Users.findByIdAndUpdate(req.body._id, {username: req.body.username, password: req.body.password, email: req.body.email, phone_number: req.body.phone_number, address: req.body.address }, function(err, user){
                if (err) throw err;
                
                res.send('Success');
            });
                
           
        });
        
    app.post('/api/user', function(req, res) {
            Users.count({username: req.body.username}, function(err, count){
                if(count>0){
                    res.send("The username is already taken.");
                }
                else{
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
          
            
        
    });

    //update
    app.post('/api/main', function(req, res) {
        
        if (req.body.id) {
            
            Flats.findByIdAndUpdate(
                req.body.id, {
                    flatname: req.body.flatname, 
                    username: req.body.username, 
                    email: req.body.email, 
                    phone_number: req.body.phone_number, 
                    zipCode: req.body.zipCode,
                    city: req.body.city,
                    address: req.body.address,
                    forSale: req.body.forSale
                }, function(err, user){
                if (err) throw err;
                
                res.send('Success');
            });
                
           
        }        
        
    });

    //add flats  to user
    app.post('/addflat/:userID', function(req, res) {
        var newFlat = Flats({
            userID: req.params.userID,
            price: req.body.price,
            numberOfRooms: req.body.numberOfRooms,
            description: req.body.description,
            email: req.body.email,
            phone_number: req.body.phone_number,
            zipCode: req.body.zipCode,
            city: req.body.city,
            address: req.body.address,
            forSale: req.body.forSale          
           });
           
           Flats.create(newFlat, function(err, results){
                if(err) throw err;
            
                res.send(results);
                
           });
    });
    
    app.get('/user/flats/:userID', function(req, res){
        
        Flats.find({userID: req.params.userID}, function(err, flats){
            if(err) throw err;
            res.send(flats);
        });
       
    });

    app.get('/flats/rooms/:numberOfRooms', function(req, res){
        
        Flats.find({numberOfRooms: { $lte: req.params.numberOfRooms}}, function(err, flats){
            if(err) throw err;
            res.send(flats);
        });
       
    });
       //find all user with querys 
       app.get('/api/user', function(req, res) {
       
        Users.find(req.query, function(err, users) {
         if(err){
             res.send("No user was found");
         }
  
            res.send(users);
        });
         
     });

     
     //find all flat with querys
     app.get('/flats/:pageid/:price', function(req, res) {
        var pageid = req.params.pageid;
        if(req.query >0)
        Flats.paginate({$and: [{price: {$lte: req.params.price}}, req.query ]}, { page: pageid, limit: 10 }, function(err, flats) {
         if(err){
             res.send("No flat was found");
         }
            
         if(pageid > 0 && flats.pages >= pageid){
            res.send(flats);
        }else{
            res.send("Invalid page");
        }
            
        });
        var price = req.params.price;
        Flats.find({price: {$lte: price}}, function(err, flats){
            if(err) throw err;
            res.send(flats);
        });
   
         
     });
     
     app.get('/flats/:pageid', function(req, res) {
        var pageid = req.params.pageid;

        Flats.paginate(req.query, { page: pageid, limit: 10 }, function(err, flats) {
         if(err){
             res.send("No flat was found");
         }
         if(pageid > 0 && flats.pages >= pageid){
            res.send(flats);
        }else{
            res.send("Invalid page");
        }
            
        });
    
     });

    app.get('/flats/price/:price', function(req, res){
        var price = req.params.price;
        Flats.find({price: {$lte: price}}, function(err, flats){
            if(err) throw err;
            res.send(flats);
        });
       
    });

    app.get('/flat/:flatID', function(req, res){
        Flats.findById({_id: req.params.flatID}, function(err, flat){
            if(err) throw err;    
            res.send(flat);
        });
    });
    
    app.delete('/api/user', function(req, res) {
        
        Users.findByIdAndRemove(req.body.id, function(err) {
            if (err) throw err;
            res.send('Success');
        })
        
    });
    
}