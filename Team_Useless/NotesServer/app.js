var express = require('express');
var app = express();
port = 4002;
var fs = require('fs');
var isRunning = false;
app.get("/", (req, res) => {
  if (!isRunning) {
    isRunning = true
    var path = require('path');
    var Youtube = require('youtube.com-extended');
    const ocrSpaceApi = require('ocr-space-api');
    var options = {
      apikey: '65020a23c888957',
      language: 'eng', // Português 
      imageFormat: 'image/png', // Image Type (Only png ou gif is acceptable at the moment i wrote this) 
      isOverlayRequired: true
    };
    // You can instantiate the youtube object using the video url 
    var youtube = Youtube('http://www.youtube.com/watch?v=' + req.query.link);
    // or you can also specify the video id 
    var minutes = Math.floor(req.query.t / 60);
    var seconds = req.query.t - minutes * 60;

    function str_pad_left(string, pad, length) {
      return (new Array(length + 1).join(pad) + string).slice(-length);
    }

    var finalTime = str_pad_left(minutes, '0', 2) + ':' + str_pad_left(seconds, '0', 2);

    youtube.snapshot(finalTime, './quote.png')
      .then(function () {
        const imageFilePath = "quote.png";
        // Run and wait the result 
        ocrSpaceApi.parseImageFromLocalFile(imageFilePath, options)
          .then(function (parsedResult) {
            fs.appendFile('notes.txt', parsedResult.parsedText, (err) => {
              if (err) throw err;
              console.log('Saved!')
            });
            console.log(parsedResult.parsedText)
          }).catch(function (err) {
            console.log('ERROR:', err);
          });

      }).catch(function (err) {
        console.log("err : ", err)
      });

    console.log(req.query);
    res.send(
      "Downloading"
    )

  }
});

var server = app.listen(port, () => {
  console.log(`listening at ${port}`);
})