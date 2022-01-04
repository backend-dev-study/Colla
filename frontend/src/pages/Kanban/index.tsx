import React from 'react';

import Header from '../../components/Header';
import KanbanCol from '../../components/KanbanCol';
import { Wrapper, KanbanAddButton, KanbanAdditional } from './style';

const statuses = ['To Do', 'Progress', 'Done'];

const Kanban = () => (
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

export default Kanban;
