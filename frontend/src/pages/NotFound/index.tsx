import React from 'react';

import Header from '../../components/Header';
import { Error } from './style';

const NotFound = () => (
    <>
        <Header />
        <Error>Page Not Found!</Error>
    </>
);

export default NotFound;
