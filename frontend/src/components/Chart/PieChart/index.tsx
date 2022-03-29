import React, { FC } from 'react';

import { TaskCountType } from '../../../types/dashboard';
import { getRandomColor } from '../../../utils/common';
import Circle from './Circle';
import { SVG } from './style';

const RADIUS = 70;
const STROKE_WIDTH = 30;

interface PropType {
    statuses: Array<TaskCountType>;
}

const sumProgress = (prevProgress: number, count: number, total: number, arr: Array<number>) => {
    arr.push(prevProgress + (count / total) * 100);
    return prevProgress + (count / total) * 100;
};

const calcProgress = (statuses: Array<TaskCountType>) => {
    const arr: Array<number> = [];
    const total = statuses.reduce((prev: number, { taskCount }) => prev + taskCount, 0);
    statuses.reduce((prev: number, { taskCount }) => sumProgress(prev, taskCount, total, arr), 0);

    return arr
        .reverse()
        .map((progress) => (
            <Circle
                key={progress}
                progress={progress}
                radius={RADIUS}
                strokeColor={getRandomColor()}
                strokeWidth={STROKE_WIDTH}
            />
        ));
};

const PieChart: FC<PropType> = ({ statuses }) => (
    <>
        <SVG viewBox="0 0 200 200">{calcProgress(statuses)}</SVG>
    </>
);

export default PieChart;
