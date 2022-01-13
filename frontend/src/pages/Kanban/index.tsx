import React from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import Header from '../../components/Header';
import KanbanCol from '../../components/KanbanCol';
import { projectNameState } from '../../stores/projectState';
import { Wrapper, KanbanAddButton, KanbanAdditional } from './style';

const statuses = ['To Do', 'Progress', 'Done'];

const Kanban = () => {
    const history = useHistory();
    const projectName = useRecoilValue(projectNameState);
    if (!projectName) {
        history.push('/');
    }

    return (
        <>
            <Header />
            <Wrapper>
                {statuses.map((value) => (
                    <KanbanCol key={value} status={value} />
                ))}
                <KanbanAdditional>
                    <KanbanAddButton />
                </KanbanAdditional>
            </Wrapper>
        </>
    );
};

export default Kanban;
