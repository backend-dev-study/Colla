import React from 'react';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';

import Kanban from './pages/Kanban';
import Login from './pages/Login';
import GlobalStyle from './styles/global';

const App = () => (
    <Router>
        <GlobalStyle />
        <Switch>
            <Route exact path="/" component={Login} />
            <Route exact path="/kanban" component={Kanban} />
        </Switch>
    </Router>
);

export default App;
