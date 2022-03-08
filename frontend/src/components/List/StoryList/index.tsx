import React, { FC, useState } from 'react';

import { StoryType } from '../../../types/roadmap';
import { StoryPeriod } from '../../Modal/StoryPeriod';
import { Issue, List, IssueContents, Title, IssueDetail } from './style';

const dummy: Array<StoryType> = [
    {
        id: 1,
        title: 'user can',
        startAt: '2022-03-08',
        endAt: '2022-03-09',
    },
    {
        id: 2,
        title: 'user can login with github',
        startAt: null,
        endAt: null,
    },
    {
        id: 3,
        title: 'user can login with githubuser can login with github user can login with github',
        startAt: '2022-03-08',
        endAt: '2022-03-09',
    },
];

interface PropType {
    handleStoryVisible: Function;
    setStory: Function;
}

export const StoryList: FC<PropType> = ({ handleStoryVisible, setStory }) => {
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
                {dummy.map((story: StoryType, idx) => {
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
