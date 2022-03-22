import React, { FC } from 'react';

import { TaskProgressType } from '../../../types/dashboard';
import { getRandomColor } from '../../../utils/common';
import Circle from './Circle';
import { SVG } from './style';

const RADIUS = 70;
const STROKE_WIDTH = 30;

interface PropType {
    statuses: Array<TaskProgressType>;
}

const sumProgress = (prevProgress: number, counts: number, total: number, arr: Array<number>) => {
    arr.push(prevProgress + (counts / total) * 100);
    return prevProgress + (counts / total) * 100;
};

const calcProgress = (statuses: Array<TaskProgressType>) => {
    const arr: Array<number> = [];
    statuses.reduce((prev: number, { statusCounts, total }) => sumProgress(prev, statusCounts, total, arr), 0);
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
