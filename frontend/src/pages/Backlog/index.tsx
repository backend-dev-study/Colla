import React from 'react';

import SearchButtonImg from '../../../public/assets/images/search-button.svg';
import Header from '../../components/Header';
import { SideBar } from '../../components/SideBar';
import { menu } from '../common';
import { Container, Feature, FeatureContainer, SearchBar, SearchIcon, SearchInput, TaskContainer } from './style';

export const Backlog = () => (
    <>
        <Header />
        <SideBar props={menu} />
        <Container>
            <FeatureContainer>
                <Feature>Story</Feature>
                <Feature>Filter</Feature>
                <Feature>Sort</Feature>
                <SearchBar>
                    <SearchInput />
                    <SearchIcon src={SearchButtonImg} />
                </SearchBar>
            </FeatureContainer>
            <TaskContainer />
        </Container>
    </>
);
