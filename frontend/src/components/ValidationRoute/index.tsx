import React from 'react';
import { Route } from 'react-router-dom';

import Login from '../../pages/Login';
import { validateUser } from '../../utils/validation';

interface Props {
    component: React.FC;
    exact: boolean;
    path: string;
}

const ValidationRoute = ({ component, ...rest }: Props) => (
    <Route {...rest} component={validateUser() ? component : Login} />
);

export default ValidationRoute;
