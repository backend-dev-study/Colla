import React from 'react';

import { BacklogFeature } from '../../components/BacklogFeature';
import Header from '../../components/Header';
import Issue from '../../components/Issue';
import { SideBar } from '../../components/SideBar';
import { Container, Wrapper } from './style';

const dummy = [
    {
        title: 'story',
        tasks: [
            {
                id: 1,
                title: 'task1',
                priority: 3,
                tags: ['backend', 'frontend', 'etc'],
            },
            {
                id: 2,
                title: 'task2',
                priority: 5,
                manager: 'https://avatars.githubusercontent.com/u/69030160?v=4',
                tags: ['bug'],
            },
            {
                id: 3,
                title: 'task3',
                priority: 1,
                tags: ['refactor'],
            },
        ],
    },
    {
        title: 'another story',
        tasks: [],
    },
];

const Backlog = () => (
    <>
        <Header />
        <SideBar />
        <BacklogFeature />
        <Container>
            <Wrapper>
                {dummy.map(({ title, tasks }: any, idx) => (
                    <>
                        <Issue key={idx} title={title} story />
                        {tasks.map(({ id, title, priority, manager, tags }: any) => (
                            <Issue key={id} title={title} priority={priority} manager={manager} tags={tags} />
                        ))}
                    </>
                ))}
            </Wrapper>
        </Container>
    </>
);

export default Backlog;
