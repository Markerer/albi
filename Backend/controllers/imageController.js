var Images = require('../models/image');
const sharp = require('sharp');
const fs = require('fs');
const checkAuth = require('../middleware/check-auth');


module.exports = function (app) {

    //BEGIN Upload Image
    const multer = require('multer')
    const path = require('path');

    const storage = multer.diskStorage({
        destination: './public/uploads/',
        filename: function (req, file, cb) {
            cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
        }
    });

    // Init upload
    const upload = multer({
        storage: storage,
        limits: { fileSize: 1000000 },

        fileFilter: function (req, file, cb) {

            checkFileType(file, cb);
        }
    }).single('image');

    //Check File Type
    function checkFileType(file, cb) {
        // Allowed ext
        const filetypes = /jpeg|jpg|png/;
        // Check ext
        const extname = filetypes.test(path.extname(file.originalname).toLowerCase());
        // Check mime
        const mimetype = filetypes.test(file.mimetype);

        if (mimetype && extname) {
            return cb(null, true);
        } else {
            cb('Error: Images Only!');
        }
    }

    //Upload image
    app.post('/flat/upload/:id', checkAuth, function (req, res) {
        var name;
        upload(req, res, function(err) {
            name = req.file.filename;
            if (err) {
                res.send('err');
            } else {
                if (req.file == undefined) {
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

                    Images.create(newImage, function (err, results) {
                        if (err) throw err;
                        res.send(results);
                    });

                }
                
                sharp('./public/uploads/' + name).resize({ width: 600 }).toBuffer(function (err, buffer) {
                    // 100 pixels wide, auto-scaled height
                    fs.writeFile('./public/uploads/' + name, buffer, function (e) {

                    });
                });
            }
        });

    });

    //END of upload Image

    //Load Image
    app.get('/flat/images/:flatId', function (req, res) {
        Images.find({ flatID: req.params.flatId }, function (err, images) {
            if (err) throw err;
            res.send(images);
            //kép átméretezés
            /*images.forEach(image => {
                sharp('./public/uploads/' + image.filename).resize({ width: 600 }).toBuffer(function (err, buffer) {
                    // 100 pixels wide, auto-scaled height
                    fs.writeFile('./public/uploads/' + image.filename, buffer, function (e) {
 
                    });
                });
 
            });*/

        }).select('_id filename');
    });

    app.get('/image/:imageID', function (req, res) {
        Images.findById({ _id: req.params.imageID }, function (err, image) {
            if (err) {
                res.send("There is no image with this ID");
            }

            /*sharp('./public/uploads/' + image.filename).resize({ width: 600 }).toBuffer(function (err, buffer) {
                // 100 pixels wide, auto-scaled height
                fs.writeFile('./public/uploads/' + image.filename, buffer, function (e) {
 
                });
            });*/
            res.send(image);
        }).select('filename');

    });


    //delete image

    app.delete('/image/:imageID', checkAuth, function (req, res) {

        Images.findByIdAndRemove(req.params.imageID, function (err) {
            if (err) {
                res.send('Unsuccessful');
            }

            res.send('Success');
        })

    });

}