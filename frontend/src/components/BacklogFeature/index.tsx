import React, { useState } from 'react';

import SearchButtonImg from '../../../public/assets/images/search-button.svg';
import { SortCriteria } from '../DropDown/SortCriteria';
import { Filter } from '../Modal/Filter';
import { Container, Feature, FeatureContainer, SearchBar, SearchIcon, SearchInput } from './style';

export const BacklogFeature = () => {
    const features = ['Story', 'Filter', 'Sort'];
    const [criteriaVisible, setCriteriaVisible] = useState<number>(0);
    const [select, setSelect] = useState('');
    const showCriteria = (idx: number) => {
        setCriteriaVisible((prev) => (prev === idx ? 0 : idx));
    };

    return (
        <Container>
            <FeatureContainer>
                {features.map((feature, idx) => (
                    <Feature key={idx}>
                        <span onClick={() => showCriteria(idx)}>{feature}</span>
                        {feature === 'Filter' && criteriaVisible === idx ? <Filter /> : null}
                        {feature === 'Sort' && criteriaVisible === idx ? (
                            <SortCriteria select={select} setSelect={setSelect} />
                        ) : null}
                    </Feature>
                ))}
                <SearchBar>
                    <SearchInput />
                    <SearchIcon src={SearchButtonImg} />
                </SearchBar>
            </FeatureContainer>
        </Container>
    );
};
