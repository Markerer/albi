'use strict';


/**
 * The main screen after the login
 * Random advertisements
 *
 * returns FlatInfoTile
 **/
exports.getRandomAds = function() {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = "";
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

