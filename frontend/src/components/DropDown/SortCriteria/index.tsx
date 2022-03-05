import React, { FC, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import ascImg from '../../../../public/assets/images/ascending.svg';
import descImg from '../../../../public/assets/images/descending.svg';
import { getTasksOrderByCreatedDate, getTasksOrderByPriority } from '../../../apis/task';
import { StateType } from '../../../types/project';
import { Container, Criteria, CriteriaTitle } from './style';

interface PropType {
    setBacklogTaskList: Function;
}

const CREATED_DATE = 'CREATED_DATE';
const PRIORITY = 'PRIORITY';

const orderImage = (asc: boolean) => (asc ? <img src={ascImg} /> : <img src={descImg} />);

export const SortCriteria: FC<PropType> = ({ setBacklogTaskList }) => {
    const { state } = useLocation<StateType>();
    const { projectId } = state;
    const [asc, setAsc] = useState(true);
    const [select, setSelect] = useState('');

    const toggleAsc = () => setAsc(!asc);

    const handleClick = (criteria: string) => {
        if (select === criteria) {
            toggleAsc();
            return;
        }
        setSelect(criteria);
        setAsc(true);
    };

    const getSortedTaskList = async () => {
        if (select === '') return;
        const res =
            select === CREATED_DATE
                ? await getTasksOrderByCreatedDate(projectId, asc)
                : await getTasksOrderByPriority(projectId, asc);

        setBacklogTaskList(res.data);
    };

    useEffect(() => {
        getSortedTaskList();
    }, [select, asc]);
    return (
        <Container>
            <Criteria onClick={() => handleClick(CREATED_DATE)}>
                <CriteriaTitle selected={select === CREATED_DATE}>생성날짜</CriteriaTitle>
                {select === CREATED_DATE ? orderImage(asc) : null}
            </Criteria>
            <Criteria onClick={() => handleClick(PRIORITY)}>
                <CriteriaTitle selected={select === PRIORITY}>중요도</CriteriaTitle>
                {select === PRIORITY ? orderImage(asc) : null}
            </Criteria>
        </Container>
    );
};
