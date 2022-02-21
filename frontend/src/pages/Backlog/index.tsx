import React from 'react';

import { BacklogFeature } from '../../components/BacklogFeature';
import Header from '../../components/Header';
import { SideBar } from '../../components/SideBar';
import { menu } from '../common';

export const Backlog = () => (
    <>
        <Header />
        <SideBar props={menu} />
        <BacklogFeature />
    </>
);
