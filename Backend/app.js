var express = require('express');
var app = express();
const ejs = require('ejs');
var mongoose = require('mongoose');
var config = require('./config');
var setupController = require('./controllers/setupController');
var apiController = require('./controllers/apiController');
var dateController = require('./controllers/dateController');
var imageController = require('./controllers/imageController');;

var port = process.env.PORT || 3000;

// Add headers
app.use(function (req, res, next) {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', 'https://localhost:4200');
    res.header("Access-Control-Allow-Origin", "*");
    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,PATCH,OPTIONS');
     // Request headers you wish to allow
    res.header(
        "Access-Control-Allow-Headers",
        "Origin, X-Requested-With, Content-Type, responseType, Accept, Authorization"
      );
      
      if (req.method === "OPTIONS") {
        res.header("Access-Control-Allow-Methods", "PUT, POST, PATCH, DELETE, GET");
        return res.status(200).json({});
      }

    // Pass to next layer of middleware
    next();
});

app.set('view engine', 'ejs');
app.use(express.static('./public/uploads'));
mongoose.connect(config.getDbConnectionString());
setupController(app);
apiController(app);
imageController(app);
dateController(app);

app.listen(port);