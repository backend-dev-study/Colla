import React, { useEffect, useState } from 'react';

import { useLocation } from 'react-router-dom';
import { getProjectMembers, getProjectStatus, getProjectTags } from '../../../apis/project';
import { Avatar, Name } from '../../Task/style';
import { Container, CriteriaElements, Element, FilterCriteria } from './style';

interface StateType {
    projectId: number;
}

interface ElementType {
    name: string;
    avatar?: string;
}

export const Filter = () => {
    const { state } = useLocation<StateType>();
    const criteriaList = ['Status', 'Manager', 'Tag'];
    const [selectedCriteria, setSelectedCriteria] = useState<number>(0);
    const [criteriaElements, setCriteriaElements] = useState<Array<ElementType>>([]);

    const changeCriteria = async (idx: number) => {
        let res = null;
        if (idx === 0) res = await getProjectStatus(state.projectId);
        else if (idx === 1) res = await getProjectMembers(state.projectId);
        else res = await getProjectTags(state.projectId);

        setSelectedCriteria(idx);
        setCriteriaElements(res.data);
    };

    useEffect(() => {
        (async () => {
            const res = await getProjectStatus(state.projectId);
            setCriteriaElements(res.data);
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
                {criteriaElements.map(({ name, avatar }, idx) => (
                    <Element key={idx}>
                        {avatar ? <Avatar src={avatar} /> : null}
                        <Name>{name}</Name>
                    </Element>
                ))}
            </CriteriaElements>
        </Container>
    );
};
