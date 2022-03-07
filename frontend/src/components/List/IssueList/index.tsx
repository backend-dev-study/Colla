import React, { useState } from 'react';

import { StoryList } from '../StoryList';
import { TaskList } from '../TaskList';
import { Container } from './style';

export const IssueList = () => {
    const [story, setStory] = useState<number>(-1);
    const [showStory, setShowStory] = useState<boolean>(true);

    const handleStoryVisible = () => {
        setShowStory((prev) => !prev);
    };

    return (
        <Container>
            {showStory ? (
                <StoryList handleStoryVisible={handleStoryVisible} setStory={setStory} />
            ) : (
                <TaskList handleStoryVisible={handleStoryVisible} story={story} />
            )}
        </Container>
    );
};
