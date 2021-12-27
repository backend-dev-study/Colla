import React from 'react';
import KanbanCol from '../../components/KanbanCol';
import { Wrapper, KanbanAddButton } from './style';

const statuses = ['To Do', 'Progress'];

const Kanban = () => (
    <Wrapper>
        {statuses.map((value) => (
            <KanbanCol key={value} status={value} />
        ))}
        <KanbanAddButton />
    </Wrapper>
);

export default Kanban;
