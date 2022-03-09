import React from 'react';

import Header from '../../components/Header';
import { IssueList } from '../../components/List/IssueList';
import { SideBar } from '../../components/SideBar';
import { Container, Wrapper } from './style';

export const Roadmap = () => (
    <>
        <Header />
        <SideBar />
        <Container>
            <Wrapper>
                <IssueList />
            </Wrapper>
        </Container>
    </>
);
