var configValues = require('./config');

module.exports = {
    
    getDbConnectionString: function() {
        return 'mongodb://' + configValues.uname + ':'+ configValues.pwd + '@ds123963.mlab.com:23963/albi';
    }
    
}