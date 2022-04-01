import React, { FC } from 'react';

import { StoryType } from '../../types/roadmap';
import { YYYYMMDDToDate } from '../../utils/common';
import TextWithHover from '../TextWithHover';
import { Story, StoryTitle, Wrapper } from './style';

interface PropType {
    storyInfo: StoryType;
    setStory: Function;
    handleStoryVisible: Function;
}

const LIMIT = 14;
const MS_DAY = 1000 * 60 * 60 * 24;

const datesToWidth = (start: string, end: string) => {
    const currentDate = new Date();
    const limitDate = new Date();
    limitDate.setDate(currentDate.getDate() + LIMIT);

    let startDate = YYYYMMDDToDate(start);
    let endDate = YYYYMMDDToDate(end);
    if (startDate.getTime() > limitDate.getTime()) return {};
    if (startDate.getTime() < currentDate.getTime()) startDate = currentDate;
    if (endDate.getTime() > limitDate.getTime()) endDate = limitDate;

    const daysToStart = Math.round((startDate.getTime() - currentDate.getTime()) / MS_DAY);
    const daysBetween = Math.round((endDate.getTime() - startDate.getTime()) / MS_DAY);
    return {
        width: daysBetween >= 0 ? daysBetween + 1 : undefined,
        beforeStart: daysToStart,
    };
};

const RoadmapStory: FC<PropType> = ({ storyInfo: { id, startAt, endAt, title }, setStory, handleStoryVisible }) => {
    const story = datesToWidth(startAt!, endAt!);

    const showStoryTasks = () => {
        setStory(id);
        handleStoryVisible();
    };

    return (
        <Wrapper onClick={showStoryTasks}>
            <StoryTitle>
                <TextWithHover text={title} hover={title} />
            </StoryTitle>
            {story!.width ? (
                <Story width={story!.width} left={story!.beforeStart}>
                    <TextWithHover text={title} hover={title} />
                </Story>
            ) : null}
        </Wrapper>
    );
};

export default RoadmapStory;
