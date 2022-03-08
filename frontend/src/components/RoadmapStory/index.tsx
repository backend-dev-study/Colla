import React, { FC } from 'react';

import { YYYYMMDDToDate } from '../../utils/common';
import { Story } from './style';

interface PropType {
    title: string;
    start: string;
    end: string;
}

const LIMIT = 14;
const MS_DAY = 1000 * 60 * 60 * 24;

const datesToWidth = (start: string, end: string) => {
    const currentDate = new Date();
    const limitDate = new Date();
    limitDate.setDate(currentDate.getDate() + LIMIT);

    let startDate = YYYYMMDDToDate(start);
    let endDate = YYYYMMDDToDate(end);
    if (startDate.getTime() > limitDate.getTime()) return undefined;
    if (startDate.getTime() < currentDate.getTime()) startDate = currentDate;
    if (endDate.getTime() > limitDate.getTime()) endDate = limitDate;

    const betweenDays = Math.ceil((endDate.getTime() - startDate.getTime()) / MS_DAY);
    return betweenDays >= 0 ? betweenDays + 1 : undefined;
};

const RoadmapStory: FC<PropType> = ({ title, start, end }) => {
    const width = datesToWidth(start, end);
    return width ? <Story width={width}>{title}</Story> : null;
};

export default RoadmapStory;
