import React, { FC } from 'react';

import { Wrapper, KanbanStatus, KanbanIssue } from './style';

interface PropType {
    status: string;
}

const KanbanCol: FC<PropType> = ({ status }) => (
    <>
        <Wrapper>
            <KanbanStatus>{status}</KanbanStatus>
            <KanbanIssue />
        </Wrapper>
    </>
);

export default KanbanCol;
