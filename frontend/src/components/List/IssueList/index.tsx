import React, { FC, useState } from 'react';

import { StoryType } from '../../../types/roadmap';
import { StoryList } from '../StoryList';
import { TaskList } from '../TaskList';
import { Container } from './style';

interface PropType {
    storyList: Array<StoryType>;
}

export const IssueList: FC<PropType> = ({ storyList }) => {
    const [story, setStory] = useState<number>(-1);
    const [showStory, setShowStory] = useState<boolean>(true);

    const handleStoryVisible = () => {
        setShowStory((prev) => !prev);
    };

    return (
        <Container>
            {showStory ? (
                <StoryList handleStoryVisible={handleStoryVisible} setStory={setStory} storyList={storyList} />
            ) : (
                <TaskList handleStoryVisible={handleStoryVisible} story={story} />
            )}
        </Container>
    );
};
