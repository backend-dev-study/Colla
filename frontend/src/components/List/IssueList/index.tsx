import React, { useState } from 'react';

import { StoryList } from '../StoryList';
import { TaskList } from '../TaskList';
import { Container } from './style';

export const IssueList = () => {
    const [showStory, setShowStory] = useState(true);

    const handleStoryVisible = () => {
        setShowStory((prev) => !prev);
    };

    return <Container>{showStory ? <StoryList /> : <TaskList showStory={handleStoryVisible} />}</Container>;
};
