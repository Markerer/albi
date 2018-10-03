'use strict';


/**
 * Create a new user
 *
 * body CreateUser New user parameters
 * no response value expected for this operation
 **/
exports.createUser = function(body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * Find a user by name
 *
 * name String Name of an user
 * returns UserInfo
 **/
exports.getUserById = function(name) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "address" : "address",
  "phone" : "phone",
  "name" : "name",
  "email" : "email",
  "age" : 0
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * Logs user into the system
 * 
 *
 * name String The user name for login
 * password String The password for login in clear text
 * returns String
 **/
exports.loginUser = function(name,password) {
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


/**
 * Logs out current logged in user session
 * 
 *
 * no response value expected for this operation
 **/
exports.logoutUser = function() {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * Updates an user
 * This can be done by the logged in user.
 *
 * name String Name of the user that needs to be updated
 * body UserInfo 
 * no response value expected for this operation
 **/
exports.updateUserInfo = function(name,body) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}

