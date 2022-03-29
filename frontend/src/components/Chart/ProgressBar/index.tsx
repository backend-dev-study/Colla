import React, { FC } from 'react';

import { TaskCountType } from '../../../types/dashboard';
import { getColorFromColorMap } from '../../../utils/common';
import { Bar, Manager, PartialHover, PartialBar, Wrapper } from './style';

interface PropType {
    managerName: string;
    statuses: Array<TaskCountType>;
    colors: Map<string, string>;
}

const calcProgress = (statuses: Array<TaskCountType>, colors: Map<string, string>) => {
    const total = statuses.reduce((prev, { taskCount }) => prev + taskCount, 0);

    return statuses.map(({ taskStatusName, taskCount }) => (
        <PartialBar
            key={taskStatusName}
            percent={(taskCount / total) * 100}
            color={getColorFromColorMap(taskStatusName, colors)}
        >
            <PartialHover>{taskStatusName}</PartialHover>
        </PartialBar>
    ));
};

const ProgressBar: FC<PropType> = ({ managerName, statuses, colors }) => (
    <Wrapper>
        <Manager>{managerName}</Manager>
        <Bar>{calcProgress(statuses, colors)}</Bar>
    </Wrapper>
);

export default ProgressBar;
