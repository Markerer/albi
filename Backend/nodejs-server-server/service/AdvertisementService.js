'use strict';


/**
 * Get the info of an advertisement
 *
 * id String The id of the ad
 * returns FlatInfo
 **/
exports.getAdvertisementById = function(id) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "owner" : "owner",
  "address" : {
    "city" : "city",
    "street" : "street"
  },
  "price" : 0,
  "description" : "description",
  "id" : "id",
  "flatImage" : [ "", "" ]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * Updates an advertisement
 * This can be done by the owner of the advertisement.
 *
 * id String The id of the ad
 * body FlatInfo The datas of the flat/house
 * no response value expected for this operation
 **/
exports.updateFlatInfo = function(id,body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * Uploading a new ad
 *
 * body FlatInfo The datas of the flat/house
 * no response value expected for this operation
 **/
exports.uploadNewAd = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}

