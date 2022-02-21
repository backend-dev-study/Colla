import React, { useState } from 'react';

import SearchButtonImg from '../../../public/assets/images/search-button.svg';
import { SortCriteria } from '../../components/DropDown/SortCriteria';
import Header from '../../components/Header';
import { SideBar } from '../../components/SideBar';
import { menu } from '../common';
import { Container, Feature, FeatureContainer, SearchBar, SearchIcon, SearchInput, TaskContainer } from './style';

export const Backlog = () => {
    const [sortCriteriaVisible, setSortCriteriaVisible] = useState<boolean>(false);

    const showSortCriteria = () => {
        setSortCriteriaVisible((prev) => !prev);
    };

    return (
        <>
            <Header />
            <SideBar props={menu} />
            <Container>
                <FeatureContainer>
                    <Feature>Story</Feature>
                    <Feature>Filter</Feature>
                    <Feature>
                        <span onClick={showSortCriteria}>Sort</span>
                        {sortCriteriaVisible ? <SortCriteria /> : null}
                    </Feature>
                    <SearchBar>
                        <SearchInput />
                        <SearchIcon src={SearchButtonImg} />
                    </SearchBar>
                </FeatureContainer>
                <TaskContainer />
            </Container>
        </>
    );
};
