import React, { FC } from 'react';

import { TaskCountType } from '../../../types/dashboard';
import { getRandomColor } from '../../../utils/common';
import { Bar, Manager, PartialHover, PartialBar, Wrapper } from './style';

interface PropType {
    managerName: string;
    statuses: Array<TaskCountType>;
}

const calcuProgress = (statuses: Array<TaskCountType>) => {
    const total = statuses.reduce((prev, { taskCount }) => prev + taskCount, 0);

    return statuses.map(({ taskStatusName, taskCount }) => (
        <PartialBar key={taskStatusName} percent={(taskCount / total) * 100} color={getRandomColor()}>
            <PartialHover>{taskStatusName}</PartialHover>
        </PartialBar>
    ));
};

const ProgressBar: FC<PropType> = ({ managerName, statuses }) => (
    <Wrapper>
        <Manager>{managerName}</Manager>
        <Bar>{calcuProgress(statuses)}</Bar>
    </Wrapper>
);

export default ProgressBar;
