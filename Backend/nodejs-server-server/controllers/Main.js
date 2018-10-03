'use strict';

var utils = require('../utils/writer.js');
var Main = require('../service/MainService');

module.exports.getRandomAds = function getRandomAds (req, res, next) {
  Main.getRandomAds()
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
