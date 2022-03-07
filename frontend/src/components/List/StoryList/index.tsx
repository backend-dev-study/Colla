import React, { FC } from 'react';

import { List, Story, StoryContents, Title } from './style';

interface StoryType {
    id: number;
    title: string;
    startAt: string;
    endAt: string;
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
        startAt: '2022-03-08',
        endAt: '2022-03-09',
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
    const handleStoryClick = (id: number) => {
        handleStoryVisible();
        setStory(id);
    };

    return (
        <div>
            <Title>스토리 목록</Title>
            <List>
                {dummy.map(({ id, title, startAt, endAt }, idx) => (
                    <Story key={idx} onClick={() => handleStoryClick(id)}>
                        <StoryContents>{title}</StoryContents>
                        <StoryContents>
                            {startAt} ~ {endAt}
                        </StoryContents>
                    </Story>
                ))}
            </List>
        </div>
    );
};
