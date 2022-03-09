import React from 'react';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import ValidationRoute from './components/ValidationRoute';
import Backlog from './pages/Backlog';
import Home from './pages/Home';
import Kanban from './pages/Kanban';
import Login from './pages/Login';
import LoginProcessing from './pages/LoginProcessing';
import NotFound from './pages/NotFound';
import Roadmap from './pages/Roadmap';
import GlobalStyle from './styles/global';

const App = () => (
    <>
        <ToastContainer position={'top-center'} />
        <DndProvider backend={HTML5Backend}>
            <Router>
                <GlobalStyle />
                <Switch>
                    <Route exact path="/" component={Login} />
                    <Route exact path="/login" component={LoginProcessing} />
                    <ValidationRoute exact path="/home" component={Home} />
                    <ValidationRoute exact path="/kanban" component={Kanban} />
                    <ValidationRoute exact path="/backlog" component={Backlog} />
                    <ValidationRoute exact path="/roadmap" component={Roadmap} />
                    <Route component={NotFound} />
                </Switch>
            </Router>
        </DndProvider>
    </>
);

export default App;
