import React, { useEffect, useState } from 'react';

import { useLocation } from 'react-router-dom';
import { getTasksGroupByStory } from '../../apis/task';
import { BacklogFeature } from '../../components/BacklogFeature';
import Header from '../../components/Header';
import Issue from '../../components/Issue';
import { SideBar } from '../../components/SideBar';
import { StoryTaskType } from '../../types/task';
import { Container, Wrapper } from './style';

interface StateType {
    projectId: number;
}

const Backlog = () => {
    const { state } = useLocation<StateType>();
    const [storyTaskList, setStoryTaskList] = useState<Array<StoryTaskType>>([]);

    useEffect(() => {
        (async () => {
            const res = await getTasksGroupByStory(state.projectId);
            setStoryTaskList(res.data);
        })();
    }, []);

    return (
        <>
            <Header />
            <SideBar />
            <BacklogFeature />
            <Container>
                <Wrapper>
                    {storyTaskList.map(({ story, taskList }: StoryTaskType, idx) => (
                        <>
                            <Issue key={idx} title={story} story />
                            {taskList.map(({ id, title, priority, managerAvatar, tags }: any) => (
                                <Issue key={id} title={title} priority={priority} manager={managerAvatar} tags={tags} />
                            ))}
                        </>
                    ))}
                </Wrapper>
            </Container>
        </>
    );
};

export default Backlog;
