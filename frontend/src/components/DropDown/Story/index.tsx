import React, { FC, useEffect, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { getProjectStories } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { StoryType } from '../../../types/kanban';
import { Story, StoryList, StoryTitle } from './style';

interface PropType {
    setStory: Function;
    setStoryVisible: Function;
}

export const StoryDropDown: FC<PropType> = ({ setStory, setStoryVisible }) => {
    const project = useRecoilValue(projectState);
    const [storyList, setStoryList] = useState<StoryType[]>([]);

    const changeStory = (story: string) => {
        setStory(story);
        setStoryVisible();
    };

    useEffect(() => {
        (async () => {
            const res = await getProjectStories(project.id);
            setStoryList(res.data);
        })();
    }, []);

    return (
        <StoryList>
            {storyList.map((story, idx) => (
                <Story key={idx} onClick={() => changeStory(story.title)}>
                    <StoryTitle>{story.title}</StoryTitle>
                </Story>
            ))}
        </StoryList>
    );
};
