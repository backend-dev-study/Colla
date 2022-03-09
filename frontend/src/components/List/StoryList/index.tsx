import React, { FC, useState } from 'react';

import { StoryType } from '../../../types/roadmap';
import { StoryPeriod } from '../../Modal/StoryPeriod';
import { Issue, List, IssueContents, Title, IssueDetail } from './style';

interface PropType {
    handleStoryVisible: Function;
    setStory: Function;
    storyList: Array<StoryType>;
}

export const StoryList: FC<PropType> = ({ handleStoryVisible, setStory, storyList }) => {
    const [selectedStory, setSelectedStory] = useState<StoryType | null>(null);
    const [storyPeriod, setStoryPeriod] = useState<boolean>(false);

    const handleStoryClick = (id: number) => {
        handleStoryVisible();
        setStory(id);
    };

    const showStoryPeriodModal = (story: StoryType) => {
        setStoryPeriod((prev) => !prev);
        setSelectedStory(story);
    };

    return (
        <>
            <Title>스토리 목록</Title>
            <List>
                {storyList.map((story: StoryType, idx) => {
                    const { id, title, startAt, endAt } = story;

                    return (
                        <Issue key={idx}>
                            <IssueContents>{title}</IssueContents>
                            <IssueContents>{startAt ? `${startAt} ~ ${endAt}` : ''}</IssueContents>
                            <IssueDetail>
                                <div onClick={() => showStoryPeriodModal(story)}>기간 설정하기</div>
                                <div onClick={() => handleStoryClick(id)}>태스크 확인하기</div>
                            </IssueDetail>
                        </Issue>
                    );
                })}
            </List>
            {storyPeriod ? <StoryPeriod story={selectedStory} showStoryPeriodModal={showStoryPeriodModal} /> : null}
        </>
    );
};
