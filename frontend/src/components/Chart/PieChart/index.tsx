import React, { FC } from 'react';

import { TaskCountType } from '../../../types/dashboard';
import { getColorFromColorMap } from '../../../utils/common';
import Circle from './Circle';
import { Svg, Colors, StatusName, Color, AbsoluteCircle, AnimatedSvg, Wrapper } from './style';

const RADIUS = 70;
const STROKE_WIDTH = 30;
let lastProgress: number = 100,
    lastStatusName: string;

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
    const curProgress = prevProgress + (count / total) * 100;
    arr.push({
        taskStatusName: statusName,
        progress: curProgress,
    });
    if (lastProgress > curProgress) {
        lastProgress = curProgress;
        lastStatusName = statusName;
    }
    return prevProgress + (count / total) * 100;
};

const calcProgress = (statuses: Array<TaskCountType>, colors: Map<string, string>) => {
    const arr: Array<ProgressType> = [];
    const total = statuses.reduce((prev: number, { taskCount }) => prev + taskCount, 0);
    statuses.reduce(
        (prev: number, { taskCount, taskStatusName }) => sumProgress(prev, taskCount, taskStatusName, total, arr),
        0,
    );

    return (
        <>
            {' '}
            <AnimatedSvg viewBox="0 0 200 200">
                <AbsoluteCircle
                    radius={RADIUS}
                    strokeColor={getColorFromColorMap(lastStatusName, colors)!}
                    strokeWidth={STROKE_WIDTH}
                    progress={lastProgress}
                />
            </AnimatedSvg>
            <Svg viewBox="0 0 200 200">
                {[
                    ...arr
                        .reverse()
                        .map(({ taskStatusName, progress }) => (
                            <Circle
                                key={progress}
                                progress={progress}
                                radius={RADIUS}
                                strokeColor={getColorFromColorMap(taskStatusName, colors)!}
                                strokeWidth={STROKE_WIDTH}
                            />
                        )),
                ]}
            </Svg>
        </>
    );
};

const PieChart: FC<PropType> = ({ statuses, colors }) => (
    <Wrapper>
        {calcProgress(statuses, colors)}
        <Colors>
            {Array.from(colors).map(([key, value]) => (
                <>
                    <StatusName>{key}</StatusName>
                    <Color color={value} />
                </>
            ))}
        </Colors>
    </Wrapper>
);

export default PieChart;
