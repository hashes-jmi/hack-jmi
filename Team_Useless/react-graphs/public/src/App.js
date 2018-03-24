import React, { Component } from 'react';
import EG from './components/EG'
import Home from './components/Home'
import Scoregraph from './components/Scoregraph'
import './App.css'
import {
  BrowserRouter as Router,
  Route
} from 'react-router-dom'
class App extends Component {


  
  render() {
    return (
      <Router>
      <div className="App">
      
       <Route exact path="/" component={Home}/>
      <Route path="/cs" component={Scoregraph}/>
      <Route path="/ve" component={EG}/>
      </div>
      </Router>
    );
  }
}

export default App;
