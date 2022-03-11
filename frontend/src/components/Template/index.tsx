import React, { FC } from 'react';

import Header from '../Header';
import { SideBar } from '../SideBar';
import { Contents } from './style';

const Template: FC = ({ children }) => (
    <>
        <Header />
        <SideBar />
        <Contents>{children}</Contents>
    </>
);

export default Template;
