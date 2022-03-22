import React from 'react';

import Circle from './Circle';
import { SVG } from './style';

const RADIUS = 70;
const STROKE_WIDTH = 30;

const PieChart = () => (
    <>
        <SVG viewBox="0 0 200 200">
            <Circle progress={100} radius={RADIUS} strokeColor={'#ffffff'} strokeWidth={STROKE_WIDTH} />
            <Circle progress={100} radius={RADIUS} strokeColor={'#6f31fe'} strokeWidth={STROKE_WIDTH} />
            <Circle progress={70} radius={RADIUS} strokeColor={'#1131fe'} strokeWidth={STROKE_WIDTH} />
            <Circle progress={50} radius={RADIUS} strokeColor={'#6ef145'} strokeWidth={STROKE_WIDTH} />
            <Circle progress={40} radius={RADIUS} strokeColor={'#bd35fe'} strokeWidth={STROKE_WIDTH} />
            <Circle progress={10} radius={RADIUS} strokeColor={'#fe31fe'} strokeWidth={STROKE_WIDTH} />
        </SVG>
    </>
);

export default PieChart;
