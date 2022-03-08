import React from 'react';

import Header from '../../components/Header';
import { SideBar } from '../../components/SideBar';
import { Container, Wrapper } from './style';

const Roadmap = () => (
    <>
        <Header />
        <SideBar />
        <Container>
            <Wrapper></Wrapper>
        </Container>
    </>
);

export default Roadmap;
