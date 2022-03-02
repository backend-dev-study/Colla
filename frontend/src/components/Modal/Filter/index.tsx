import React, { FC, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import CheckImg from '../../../../public/assets/images/down.png';
import { getProjectMembers, getProjectStatus, getProjectTags } from '../../../apis/project';
import { getTasksFilterByManager, getTasksFilterByStatus, getTasksFilterByTags } from '../../../apis/task';
import { StateType } from '../../../types/project';
import { Avatar, Name } from '../../Task/style';
import { CheckMark, Container, CriteriaElements, Element, FilterCriteria } from './style';

interface ElementType {
    name: string;
    avatar?: string;
}

interface PropType {
    setBacklogTaskList: Function;
}

export const Filter: FC<PropType> = ({ setBacklogTaskList }) => {
    const { state } = useLocation<StateType>();
    const { projectId } = state;
    const criteriaList = ['Status', 'Manager', 'Tag'];
    const [selectedCriteria, setSelectedCriteria] = useState<number>(0);
    const [criteriaOptions, setCriteriaOptions] = useState<Array<ElementType>>([]);
    const [selectedOptions, setSelectedOptions] = useState<Array<number>>([]);

    const changeCriteria = async (idx: number) => {
        let res = null;
        if (idx === 0) res = await getProjectStatus(projectId);
        else if (idx === 1) res = await getProjectMembers(projectId);
        else res = await getProjectTags(projectId);

        setSelectedOptions([]);

        setSelectedCriteria(idx);
        setCriteriaOptions(res.data);
    };

    const getTasksAboutCriteria = async (idx: number) => {
        const list = selectedOptions.includes(idx)
            ? selectedOptions.filter((el) => el !== idx)
            : [...selectedOptions, idx];
        setSelectedOptions(list);
        const options = list.map((idx) => criteriaOptions[idx].name).join();

        let res = null;
        if (selectedCriteria === 0) res = await getTasksFilterByStatus(projectId, options);
        else if (selectedCriteria === 1) res = await getTasksFilterByManager(projectId, options);
        else res = await getTasksFilterByTags(projectId, options);

        setBacklogTaskList(res.data);
    };

    useEffect(() => {
        (async () => {
            const res = await getProjectStatus(state.projectId);
            setCriteriaOptions(res.data);
        })();
    }, []);

    return (
        <Container>
            <FilterCriteria>
                {criteriaList.map((criteria, idx) => (
                    <div
                        key={idx}
                        className={selectedCriteria === idx ? 'selected' : ''}
                        onClick={() => changeCriteria(idx)}
                    >
                        {criteria}
                    </div>
                ))}
            </FilterCriteria>
            <CriteriaElements>
                {criteriaOptions.map(({ name, avatar }, idx) => (
                    <Element key={idx} onClick={() => getTasksAboutCriteria(idx)}>
                        {avatar ? <Avatar src={avatar} /> : null}
                        <Name>{name}</Name>
                        {selectedOptions.includes(idx) ? <CheckMark src={CheckImg} /> : null}
                    </Element>
                ))}
            </CriteriaElements>
        </Container>
    );
};
