import React, { FC, useState } from 'react';

import ascImg from '../../../../public/assets/images/ascending.svg';
import descImg from '../../../../public/assets/images/descending.svg';
import { Container, Criteria, CriteriaTitle } from './style';

interface PropType {
    select: string;
    setSelect: Function;
}

const CREATED_AT = 'SORT.CREATED_AT';
const PRIORITY = 'SORT.PRIORITY';

const orderImage = (asc: boolean) => (asc ? <img src={ascImg} /> : <img src={descImg} />);

export const SortCriteria: FC<PropType> = ({ select, setSelect }) => {
    const [asc, setAsc] = useState(true);

    const toggleAsc = () => setAsc(!asc);

    const handleClick = (criteria: string) => {
        if (select === criteria) {
            toggleAsc();
            return;
        }
        setSelect(criteria);
        setAsc(true);
    };

    return (
        <Container>
            <Criteria onClick={() => handleClick(CREATED_AT)}>
                <CriteriaTitle selected={select === CREATED_AT}>생성날짜</CriteriaTitle>
                {select === CREATED_AT ? orderImage(asc) : null}
            </Criteria>
            <Criteria onClick={() => handleClick(PRIORITY)}>
                <CriteriaTitle selected={select === PRIORITY}>중요도</CriteriaTitle>
                {select === PRIORITY ? orderImage(asc) : null}
            </Criteria>
        </Container>
    );
};
