var express=require('express');
var socket=require('socket.io');
var sentiment = require('sentiment');
var firebase = require('firebase');

var fireapp = firebase.initializeApp({ 
apiKey: "AIzaSyCOmubrc3gEd6LOW5UfRH5LVaL-GFgRCgk",
authDomain: "not-so-awesome-project-45a2e.firebaseapp.com",
databaseURL: "https://not-so-awesome-project-45a2e.firebaseio.com",
projectId: "not-so-awesome-project-45a2e",
storageBucket: "not-so-awesome-project-45a2e.appspot.com",
messagingSenderId: "481329884022"})
var ref = fireapp.database().ref('/nlpScores')
//App setup
var port = process.env.PORT || 4000;
var app=express();
var server=app.listen(port,function(){
    console.log('listening to request on port 4000')
});
var allowCrossDomain = function (req, res, next) {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
next();
}

app.use(allowCrossDomain);
// serve static files
app.use(express.static('public'));

//Socket setup
//socket is a function which takes which server we want to work with
var io=socket(server);

//socket.io is listening for a connection
io.on('connection',function(socket){
    console.log('Made socket connection',socket.id);

    //listening for event
    socket.on('chat',function(data){
        //refering to all sockets
        io.sockets.emit('chat',data);
        /**
         * Calculates the Sentiment of the message Sent by user
         */
    ref.push({ score : sentiment(data.message).score})
    console.log({ score : sentiment(data.message).score})
    });

    socket.on('typing',function(data){
        //broadcasting message to all expect the one who is typing
        socket.broadcast.emit('typing',data);
    });
});
