import React, { FC } from 'react';

import { TaskCountType } from '../../../types/dashboard';
import { getColorFromColorMap } from '../../../utils/common';
import Circle from './Circle';
import { SVG } from './style';

const RADIUS = 70;
const STROKE_WIDTH = 30;

interface PropType {
    statuses: Array<TaskCountType>;
    colors: Map<string, string>;
}

interface ProgressType {
    taskStatusName: string;
    progress: number;
}

const sumProgress = (
    prevProgress: number,
    count: number,
    statusName: string,
    total: number,
    arr: Array<ProgressType>,
) => {
    arr.push({
        taskStatusName: statusName,
        progress: prevProgress + (count / total) * 100,
    });
    return prevProgress + (count / total) * 100;
};

const calcProgress = (statuses: Array<TaskCountType>, colors: Map<string, string>) => {
    const arr: Array<ProgressType> = [];
    const total = statuses.reduce((prev: number, { taskCount }) => prev + taskCount, 0);
    statuses.reduce(
        (prev: number, { taskCount, taskStatusName }) => sumProgress(prev, taskCount, taskStatusName, total, arr),
        0,
    );

    return arr
        .reverse()
        .map(({ taskStatusName, progress }) => (
            <Circle
                key={progress}
                progress={progress}
                radius={RADIUS}
                strokeColor={getColorFromColorMap(taskStatusName, colors)}
                strokeWidth={STROKE_WIDTH}
            />
        ));
};

const PieChart: FC<PropType> = ({ statuses, colors }) => (
    <>
        <SVG viewBox="0 0 200 200">{calcProgress(statuses, colors)}</SVG>
    </>
);

export default PieChart;
