import React, { FC } from 'react';

import { TaskProgressType } from '../../../types/dashboard';
import { getRandomColor } from '../../../utils/common';
import { Bar, Manager, PartialHover, PartialBar, Wrapper } from './style';

interface PropType {
    managerName: string;
    statuses: Array<TaskProgressType>;
}

const ProgressBar: FC<PropType> = ({ managerName, statuses }) => (
    <Wrapper>
        <Manager>{managerName}</Manager>
        <Bar>
            {statuses.map(({ statusName, statusCounts, total }) => (
                <PartialBar key={statusName} percent={(statusCounts / total) * 100} color={getRandomColor()}>
                    <PartialHover>{statusName}</PartialHover>
                </PartialBar>
            ))}
        </Bar>
    </Wrapper>
);

export default ProgressBar;
