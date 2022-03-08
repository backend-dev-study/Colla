import React, { FC, useState } from 'react';

import { StoryPeriod } from '../../Modal/StoryPeriod';
import { Issue, List, IssueContents, Title, IssueDetail } from './style';

interface StoryType {
    id: number;
    title: string;
    startAt: string | null;
    endAt: string | null;
}

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
    const [storyPeriod, setStoryPeriod] = useState<boolean>(false);

    const handleStoryClick = (id: number) => {
        handleStoryVisible();
        setStory(id);
    };

    const showStoryPeriodModal = () => {
        setStoryPeriod((prev) => !prev);
    };

    return (
        <>
            <Title>스토리 목록</Title>
            <List>
                {dummy.map(({ id, title, startAt, endAt }, idx) => (
                    <Issue key={idx}>
                        <IssueContents>{title}</IssueContents>
                        <IssueContents>{startAt ? `${startAt} ~ ${endAt}` : ''}</IssueContents>
                        <IssueDetail>
                            <div onClick={showStoryPeriodModal}>기간 설정하기</div>
                            <div onClick={() => handleStoryClick(id)}>태스크 확인하기</div>
                        </IssueDetail>
                    </Issue>
                ))}
            </List>
            {storyPeriod ? <StoryPeriod showStoryPeriodModal={showStoryPeriodModal} /> : null}
        </>
    );
};
