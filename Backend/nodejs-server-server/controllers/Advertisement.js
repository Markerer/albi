'use strict';

var utils = require('../utils/writer.js');
var Advertisement = require('../service/AdvertisementService');

module.exports.getAdvertisementById = function getAdvertisementById (req, res, next) {
  var id = req.swagger.params['id'].value;
  Advertisement.getAdvertisementById(id)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.updateFlatInfo = function updateFlatInfo (req, res, next) {
  var id = req.swagger.params['id'].value;
  var body = req.swagger.params['body'].value;
  Advertisement.updateFlatInfo(id,body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.uploadNewAd = function uploadNewAd (req, res, next) {
  var body = req.swagger.params['body'].value;
  Advertisement.uploadNewAd(body)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
