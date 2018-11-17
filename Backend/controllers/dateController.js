var Dates = require('../models/date');

module.exports = function(app) {

    app.get('/date/:dateID', function (req, res) {
        Dates.findById({ _id: req.params.dateID }, function (err, date) {
            if (err) {
                res.send("There is no date with this ID");
            }

            res.send(date);
        });

    });

    app.delete('/date/:dateID', function (req, res) {

        Dates.findByIdAndRemove(req.params.dateID, function (err) {
            if (err) {
                res.send('Unsuccessful');
            }

            res.send('Success');
        })

    });

    app.post('/date/:flatID', function(req, res){
        var date = req.body.date;
        var flatID = req.params.flatID;
        
        Dates.count({date: date}, function(err, count){
           
            if(count > 0){
               
                Dates.findOneAndUpdate({date: date},{
                        $inc: {counter: 1}
                }, function(err, date){
                    
                 if(err) throw err;
                 res.send(date);
                    
                });
            } else{
                var newDate = Dates({
                    flatID: flatID,
                    counter: 1,
                    date: date
                });

                Dates.create(newDate, function(err, result){
                    if(err) throw err;
                    res.send(result);
                });
            }
        });
    });

    app.get('/flat/dates/:flatId', function (req, res) {
        Dates.find({ flatID: req.params.flatId }, function (err, dates) {
            if (err){
                res.send("No date was found");
            }
            res.send(dates);


        });
    });


}