var accessToken = "5f290f2e6f28476591774475a3127766";
var baseUrl = "https://api.api.ai/v1/";
$(document).ready(function () {


  $("#notes").click(() => {
    Materialize.toast('Saving Your Notes ! ', 2000)
    $.ajax({
      type: "GET",
      url: "http://localhost:4002/?link=cKxRvEZd3Mw&t=" + Math.floor(player.getCurrentTime()),
      contentType: "application/txt; charset=utf-8",
      dataType: 'jsonp',
      headers: {
        "Authorization": "Bearer " + accessToken
      },
      // data: JSON.stringify({ query: text, lang: "en", sessionId: "somerandomthing" }),
      success: function (data) {
        console.log(data);

      }
    });


  });
  $("#input").keypress(function (event) {
    if (event.which == 13) {
      event.preventDefault();
      send();
    }
  });
  $("#rec").click(function (event) {
    switchRecognition();
  });
});
var recognition;
var INDEX = 0;



function generate_message(msg, type) {
  INDEX++;
  var str = "";
  str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
  str += "          <span class=\"msg-avatar\">";
  str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
  str += "          <\/span>";
  str += "          <div class=\"cm-msg-text\">";
  str += msg;
  str += "          <\/div>";
  str += "        <\/div>";
  $(".chat-logs").append(str);
  $("#cm-msg-" + INDEX).hide().fadeIn(300);
  if (type == 'self') {
    $("#chat-input").val('');
  }
  $(".chat-logs").stop().animate({
    scrollTop: $(".chat-logs")[0].scrollHeight
  }, 1000);
}


function startRecognition() {
  recognition = new webkitSpeechRecognition();
  recognition.onstart = function (event) {
    updateRec();
  };
  recognition.onresult = function (event) {
    var text = "";
    for (var i = event.resultIndex; i < event.results.length; ++i) {
      text += event.results[i][0].transcript;
    }
    // console.log(text)
    $('#answer').text(text);
    if (text === "orange") {
      Materialize.toast('You nailed it!!!! ', 2000);

    } else {

      Materialize.toast('Better luck next time   ! ', 6000);


    }
    setTimeout(() => {
      $('#modal2').modal('close');
      player.playVideo()
    }, 2000)
    stopRecognition();
  };
  recognition.onend = function () {
    stopRecognition();
  };
  recognition.lang = "en-US";
  recognition.start();
}

function stopRecognition() {
  if (recognition) {
    recognition.stop();
    recognition = null;
  }
  updateRec();
}

function switchRecognition() {
  if (recognition) {
    stopRecognition();
  } else {
    startRecognition();
  }
}


function setInput(text) {
  $("#input").val(text);
  send();
}

function updateRec() {
  $("#rec").text(recognition ? "Stop" : "Speak");
}

function setResponse(val) {
  $("#response").text(val);
}
$(function () {

  $("#chat-submit").click(function (e) {
    e.preventDefault();
    var msg = $("#chat-input").val();
    if (msg.trim() == '') {
      return false;
    } else {
      generate_message(msg, 'self');
      send(msg);
      var buttons = [{
        name: 'Existing User',
        value: 'existing'
      },
      {
        name: 'New User',
        value: 'new'
      }
      ];

      // setTimeout(function () {
      //   generate_message(msg, 'user');
      // }, 1000)
    }
  })

  function send(text) {

    $.ajax({
      type: "POST",
      url: baseUrl + "query?v=20150910",
      contentType: "application/json; charset=utf-8",
      dataType: "json",
      headers: {
        "Authorization": "Bearer " + accessToken
      },
      data: JSON.stringify({
        query: text,
        lang: "en",
        sessionId: "somerandomthing"
      }),
      success: function (data) {

        var obj = data.result;
        var fullfil = obj.fulfillment;
        var messgs = fullfil.messages;
        messgs.forEach(function (element) {
          generate_message(element.speech, 'user');
        }, this);


      },
      error: function () {
        setResponse("Internal Server Error");
      }
    });
    setResponse("Loading...");
  }


  function generate_message(msg, type) {
    INDEX++;
    var str = "";
    str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
    str += "          <span class=\"msg-avatar\">";
    str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
    str += "          <\/span>";
    str += "          <div class=\"cm-msg-text\">";
    str += msg;
    str += "          <\/div>";
    str += "        <\/div>";
    $(".chat-logs").append(str);
    $("#cm-msg-" + INDEX).hide().fadeIn(300);
    if (type == 'self') {
      $("#chat-input").val('');
    }
    $(".chat-logs").stop().animate({
      scrollTop: $(".chat-logs")[0].scrollHeight
    }, 1000);
  }

  function generate_button_message(msg, buttons) {
    /* Buttons should be object array 
      [
        {
          name: 'Existing User',
          value: 'existing'
        },
        {
          name: 'New User',
          value: 'new'
        }
      ]
      */
      INDEX++;
      var btn_obj = buttons.map(function (button) {
        return "              <li class=\"button\"><a href=\"javascript:;\" class=\"btn btn-primary chat-btn\" chat-value=\"" + button.value + "\">" + button.name + "<\/a><\/li>";
      }).join('');
      var str = "";
      str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg user\">";
      str += "          <span class=\"msg-avatar\">";
      str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
      str += "          <\/span>";
      str += "          <div class=\"cm-msg-text\">";
      str += msg;
      str += "          <\/div>";
      str += "          <div class=\"cm-msg-button\">";
      str += "            <ul>";
      str += btn_obj;
      str += "            <\/ul>";
      str += "          <\/div>";
      str += "        <\/div>";
      $(".chat-logs").append(str);
      $("#cm-msg-" + INDEX).hide().fadeIn(300);
      $(".chat-logs").stop().animate({
        scrollTop: $(".chat-logs")[0].scrollHeight
      }, 1000);
      $("#chat-input").attr("disabled", true);
    }

    $(document).delegate(".chat-btn", "click", function () {
      var value = $(this).attr("chat-value");
      var name = $(this).html();
      $("#chat-input").attr("disabled", false);
      generate_message(name, 'self');
    })

    $("#chat-circle").click(function () {
      $("#chat-circle").toggle('scale');
      $(".chat-box").toggle('scale');
    })

    $(".chat-box-toggle").click(function () {
      $("#chat-circle").toggle('scale');
      $(".chat-box").toggle('scale');
    })

  })


$(".button-collapse").sideNav();
$('.control').click(function () {
  $('body').addClass('search-active');
  $('.input-search').focus();
});
$('.mainaddcomments').hide();
$('.icon-close').click(function () {
  $('body').removeClass('search-active');
});
$('li.addcomments').on('click tap', function () {

  $('.threadComment').hide();
  $('.mainaddcomments').show();

});

$('li.comments').on('click tap', function () {

  $('.threadComment').show();
  $('.mainaddcomments').hide();
});

/**
 * Firebase
 */
// Initialize Firebase
$(document).ready(function () {

  // $(aja)
  $('.modal').modal();
  $('.collapsible').collapsible();
  $('#floatbtn').click(() => {

    $('.collapsible').toggle(function () {
      $(".collapsible").removeClass("active");

    }, function () {
      $(".collapsible").addClass("active");
    });
    $('.mainaddcomment').toggle(function () {
      $(".mainaddcomment").removeClass("active");

    }, function () {
      $(".mainaddcomment").addClass("active");
    });

  })


});


var config = {
  apiKey: "AIzaSyCOmubrc3gEd6LOW5UfRH5LVaL-GFgRCgk",
  authDomain: "not-so-awesome-project-45a2e.firebaseapp.com",
  databaseURL: "https://not-so-awesome-project-45a2e.firebaseio.com",
  projectId: "not-so-awesome-project-45a2e",
  storageBucket: "not-so-awesome-project-45a2e.appspot.com",
  messagingSenderId: "481329884022"
};
firebase.initializeApp(config);
firebase.database().ref('/emotions').remove()

/*
   SDK Needs to create video and canvas nodes in the DOM in order to function
   Here we are adding those nodes a predefined div.
   */
   var divRoot = $("#affdex_elements")[0];

// The captured frame's width in pixels
var width = 320;

// The captured frame's height in pixels
var height = 240;

/*
   Face detector configuration - If not specified, defaults to
   affdex.FaceDetectorMode.LARGE_FACES
   affdex.FaceDetectorMode.LARGE_FACES=Faces occupying large portions of the frame
   affdex.FaceDetectorMode.SMALL_FACES=Faces occupying small portions of the frame
   */
   var faceMode = affdex.FaceDetectorMode.LARGE_FACES;
   var isPlaying = false
//Construct a CameraDetector and specify the image width / height and face detector mode.
var detector = new affdex.CameraDetector(divRoot, width, height, faceMode);
// * 
//   onImageResults success is called when a frame is processed successfully and receives 3 parameters:
//   - Faces: Dictionary of faces in the frame keyed by the face id.
//            For each face id, the values of detected emotions, expressions, appearane metrics 
//            and coordinates of the feature points
//   - image: An imageData object containing the pixel values for the processed frame.
//   - timestamp: The timestamp of the captured image in seconds.
// */


var player;

function onYouTubeIframeAPIReady() {
  player = new YT.Player('player', {
    height: '600',
    width: '1000',
    videoId: 'cKxRvEZd3Mw',
    events: {
      'onReady': onPlayerReady,
      'onStateChange': onPlayerStateChange
    }
  });
}






var engagement_arr = []
var joyness_arr = []
var keys = []
var key = 0
var radpoints = []
var low_engage = 0;
var isAlreadypoped = false




detector.addEventListener("onImageResultsSuccess", function (faces, image, timestamp) {

  if (faces['0'] != null && isPlaying) {
    var emotions1 = faces['0'].emotions;
    key = key + 1
    firebase.database().ref('/emotions').push(emotions1);
    engagement_arr.push(emotions1.engagement)

    // console.log(emotions1.engagement)

    if (emotions1.engagement === 0) {
      low_engage = low_engage + 1;
    }

    console.log(low_engage);

    if (low_engage === 10 && !isAlreadypoped) {
      isAlreadypoped = true
      document.getElementById('chat-circle').click();
      low_engage = 0;
      player.pauseVideo();
      generate_message("Geting Boared ?", 'user');
      generate_message(" Go out take a nap or try something more funnier like https://www.youtube.com/watch?v=JLqs0qk54q8 ", 'user');
      setTimeout(() => {
        document.getElementById('chat-circle').click();
        player.playVideo()
        isAlreadypoped = false
      }, 6000);
    }
    // console.log(emotions1.joy)
    joyness_arr.push(emotions1.joy);
    keys.push("");
    radpoints.push(0)

  } // emotions.

});
setInterval(() => low_engage = low_engage = 0, 15000);

/* 
  onImageResults success receives 3 parameters:
  - image: An imageData object containing the pixel values for the processed frame.
  - timestamp: An imageData object contain the pixel values for the processed frame.
  - err_detail: A string contains the encountered exception.
  */
  detector.addEventListener("onImageResultsFailure", function (image, timestamp, err_detail) {});
  detector.addEventListener("onWebcamConnectSuccess", function () {
    console.log("I was able to connect to the camera successfully.");
  });

  detector.addEventListener("onWebcamConnectFailure", function () {
    console.log("I've failed to connect to the camera :(");
  });


  detector.detectAllExpressions();
  detector.detectAllEmotions();
  detector.start();

  var tag = document.createElement('script');

  tag.src = 'https://www.youtube.com/iframe_api';
  var firstScriptTag = document.getElementsByTagName('script')[0];
  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// 3. This function creates an <iframe> (and YouTube player)
//    after the API code downloads.



var isStarted = false;
// 4. The API will call this function when the video player is ready.
function onPlayerReady(event) {
  console.log(event);
  $('#modal1').modal('open');
  setTimeout(() => {
    $('#modal1').modal('close');
    event.target.playVideo();
  }, 3000);

  setInterval(() => {
    if (Math.floor(player.getCurrentTime()) === 346) {
      if (isStarted === false) {
        $('#modal2').modal('open');
        player.pauseVideo();
        startRecognition();
        isStarted = true;
      }


    }
  }, 1000);
}

var done = false;

function onPlayerStateChange(event) {
  if (event.data === 1) {
    isPlaying = true
    console.log('Playing');
  }
  if (event.data === 2) {
    isPlaying = false

    console.log('Paused')
  }
  if (event.data === 0) {
    isPlaying = false
    console.log('Ended')
    detector.stop();
    console.log(engagement_arr);
    $('#modal1').modal('open');
    Materialize.toast('Wnna give Your FeedBack to Us? ', 2000)
  }
}

function stopVideo() {
  player.stopVideo();

}