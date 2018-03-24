var firebase = require('firebase');
var config = {
  apiKey: "AIzaSyCOmubrc3gEd6LOW5UfRH5LVaL-GFgRCgk",
  authDomain: "not-so-awesome-project-45a2e.firebaseapp.com",
  databaseURL: "https://not-so-awesome-project-45a2e.firebaseio.com",
  projectId: "not-so-awesome-project-45a2e",
  storageBucket: "not-so-awesome-project-45a2e.appspot.com",
  messagingSenderId: "481329884022"
};
firebase.initializeApp(config);
 export const dbref =  firebase.database()