import React from 'react';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import ValidationRoute from './components/ValidationRoute';
import Home from './pages/Home';
import Kanban from './pages/Kanban';
import Login from './pages/Login';
import LoginProcessing from './pages/LoginProcessing';
import GlobalStyle from './styles/global';

const App = () => (
    <DndProvider backend={HTML5Backend}>
        <Router>
            <GlobalStyle />
            <Switch>
                <Route exact path="/" component={Login} />
                <Route exact path="/login" component={LoginProcessing} />
                <ValidationRoute exact path="/kanban" component={Kanban} />
                <ValidationRoute exact path="/home" component={Home} />
            </Switch>
        </Router>
    </DndProvider>
);

export default App;
