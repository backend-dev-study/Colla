import React, { FC } from 'react';

import { ProgressCircle } from './style';

interface PropType {
    progress: number;
    radius: number;
    strokeColor: string;
    strokeWidth: number;
}

const Circle: FC<PropType> = ({ progress, radius, strokeColor, strokeWidth }) => (
    <ProgressCircle radius={radius} progress={progress} strokeWidth={strokeWidth} strokeColor={strokeColor} />
);

export default Circle;
