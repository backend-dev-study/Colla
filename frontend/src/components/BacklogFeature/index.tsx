import React, { FC, useState } from 'react';

import { useLocation } from 'react-router-dom';
import SearchButtonImg from '../../../public/assets/images/search-button.svg';
import { getTasksGroupByStory, searchTasksByKeyword } from '../../apis/task';
import { StateType } from '../../types/project';
import { SortCriteria } from '../DropDown/SortCriteria';
import { Filter } from '../Modal/Filter';
import { Container, Feature, FeatureContainer, Features, SearchBar, SearchIcon, SearchInput } from './style';

interface PropType {
    setBacklogTaskList: Function;
}

export const BacklogFeature: FC<PropType> = ({ setBacklogTaskList }) => {
    const { state } = useLocation<StateType>();
    const features = ['Story', 'Filter', 'Sort'];
    const [criteriaVisible, setCriteriaVisible] = useState<number>(0);
    const [searchKeyword, setSearchKeyword] = useState('');

    const showCriteria = (idx: number) => {
        setCriteriaVisible((prev) => (prev === idx ? 0 : idx));
    };

    const getTasksAboutStory = async () => {
        const res = await getTasksGroupByStory(state.projectId);
        setBacklogTaskList(res.data);
    };

    const changeSearchKeyword = (e: React.ChangeEvent) => {
        setSearchKeyword((e.target as HTMLInputElement).value);
    };

    const searchTasks = async () => {
        const res = await searchTasksByKeyword(state.projectId, searchKeyword);
        setBacklogTaskList(res.data);
    };

    const handleEnter = (e: React.KeyboardEvent) => {
        if (e.key !== 'Enter') return;
        searchTasks();
    };

    const handleClick = () => searchTasks();

    return (
        <Container>
            <FeatureContainer>
                <Features>
                    {features.map((feature, idx) => (
                        <Feature key={idx}>
                            <span onClick={feature === 'Story' ? () => getTasksAboutStory() : () => showCriteria(idx)}>
                                {feature}
                            </span>
                            {feature === 'Filter' && criteriaVisible === idx ? (
                                <Filter setBacklogTaskList={setBacklogTaskList} />
                            ) : null}
                            {feature === 'Sort' && criteriaVisible === idx ? (
                                <SortCriteria setBacklogTaskList={setBacklogTaskList} />
                            ) : null}
                        </Feature>
                    ))}
                    <SearchBar>
                        <SearchInput
                            value={searchKeyword}
                            onChange={(e) => changeSearchKeyword(e)}
                            onKeyPress={handleEnter}
                        />
                        <SearchIcon src={SearchButtonImg} onClick={handleClick} />
                    </SearchBar>
                </Features>
            </FeatureContainer>
        </Container>
    );
};
