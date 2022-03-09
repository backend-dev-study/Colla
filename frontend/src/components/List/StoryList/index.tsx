import React, { FC, useEffect, useState } from 'react';

import { useLocation } from 'react-router-dom';
import { getProjectStories } from '../../../apis/story';
import { StateType } from '../../../types/project';
import { StoryType } from '../../../types/roadmap';
import { StoryPeriod } from '../../Modal/StoryPeriod';
import { Issue, List, IssueContents, Title, IssueDetail } from './style';

interface PropType {
    handleStoryVisible: Function;
    setStory: Function;
}

export const StoryList: FC<PropType> = ({ handleStoryVisible, setStory }) => {
    const { state } = useLocation<StateType>();
    const [storyList, setStoryList] = useState<Array<StoryType>>([]);
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

    useEffect(() => {
        (async () => {
            const res = await getProjectStories(state.projectId);
            setStoryList(res.data);
        })();
    }, []);

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
