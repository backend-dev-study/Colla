import React, { FC } from 'react';

import { ROADMAP_DATES_LIMIT } from '../../constants';
import { StoryType } from '../../types/roadmap';
import { YYYYMMDDToDate } from '../../utils/common';
import TextWithHover from '../TextWithHover';
import { Story, StoryTitle } from './style';

interface PropType {
    storyInfo: StoryType;
    setStory: Function;
    handleStoryVisible: Function;
    startRow: number;
}

const MS_DAY = 1000 * 60 * 60 * 24;

const datesToWidth = (start: string, end: string) => {
    const currentDate = new Date();
    const limitDate = new Date();
    limitDate.setDate(currentDate.getDate() + ROADMAP_DATES_LIMIT);
    currentDate.setHours(0, 0, 0, 0);
    limitDate.setHours(0, 0, 0, 0);

    let startDate = YYYYMMDDToDate(start);
    let endDate = YYYYMMDDToDate(end);
    if (startDate.getTime() > limitDate.getTime()) return {};
    if (startDate.getTime() < currentDate.getTime()) startDate = currentDate;
    if (endDate.getTime() > limitDate.getTime()) endDate = limitDate;
    const daysToStart = Math.round((startDate.getTime() - currentDate.getTime()) / MS_DAY);
    const daysBetween = Math.round((endDate.getTime() - startDate.getTime()) / MS_DAY) + 1;

    return {
        width: daysBetween >= 0 ? daysBetween : undefined,
        beforeStart: daysToStart,
    };
};

const RoadmapStory: FC<PropType> = ({
    storyInfo: { id, startAt, endAt, title },
    setStory,
    handleStoryVisible,
    startRow,
}) => {
    const story = datesToWidth(startAt!, endAt!);

    const showStoryTasks = () => {
        setStory(id);
        handleStoryVisible();
    };

    return (
        <>
            <StoryTitle row={startRow}>
                <TextWithHover text={title} hover={title} />
            </StoryTitle>
            {story!.width ? (
                <Story row={startRow} width={story!.width} left={story!.beforeStart} onClick={showStoryTasks}>
                    <TextWithHover text={title} hover={title} />
                </Story>
            ) : null}
        </>
    );
};

export default RoadmapStory;
