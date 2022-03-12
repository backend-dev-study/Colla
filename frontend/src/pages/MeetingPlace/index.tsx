import React from 'react';

import Header from '../../components/Header';
import { Map } from '../../components/Map';
import { SideBar } from '../../components/SideBar';
import { Container, Temp, Wrapper } from './style';

const MeetingPlace = () => (
    <>
        <Header />
        <SideBar />
        <Container>
            <Wrapper>
                <Temp />
                <Map />
            </Wrapper>
        </Container>
    </>
);

export default MeetingPlace;
