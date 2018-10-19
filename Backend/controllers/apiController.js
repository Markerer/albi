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
           
            res.send(users);  
          });
    });
    
    //list all flats
    app.get('/api/main/', function(req, res){
        Flats.find({}, function(err, flats){
            res.send(flats);
        })
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
                console.log(images);
                console.log(images.path);  
                res.send(images);
            }).select('path');
    })

    //search by username
    app.get('/api/users/:uname', function(req, res) {
        
        Users.find({ username: req.params.uname }, function(err, users) {
            if (err) throw err;
            
            res.send(users);
        });
        
    });

    //search user by id
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

    //update, add flats
    app.post('/api/main', function(req, res) {
        
        if (req.body.id) {
            
            Flats.findByIdAndUpdate(req.body.id, {flatname: req.body.flatname, username: req.body.username, email: req.body.email, phone_number: req.body.phone_number, address: req.body.address, hasAttachment: req.body.hasAttachment, imageID: req.body.imageID }, function(err, user){
                if (err) throw err;
                
                res.send('Success');
            });
                
           
        }        
        
    });

    app.post('/addflat/:userID', function(req, res) {
        var newFlat = Flats({
            userID: req.params.userID,
            price: req.body.price,
            numberOfRooms: req.body.numberOfRooms,
            description: req.body.description,
            email: req.body.email,
            phone_number: req.body.email,
            address: req.body.address,
            hasAttachment: false,
            
           });
           
           Flats.create(newFlat, function(err, results){
                if(err) throw err;
            
                res.send(results);
                
           });
    });
            
    
    app.delete('/api/user', function(req, res) {
        
        Users.findByIdAndRemove(req.body.id, function(err) {
            if (err) throw err;
            res.send('Success');
        })
        
    });
    
}