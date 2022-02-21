import React from 'react';

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
            },
            {
                id: 2,
                title: 'task2',
                priority: 3,
            },
            {
                id: 3,
                title: 'task3',
                priority: 3,
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
        <Container>
            <Wrapper>
                {dummy.map(({ title, tasks }: any, idx) => (
                    <>
                        <Issue key={idx} title={title} story />
                        {tasks.map(({ id, title }: any) => (
                            <Issue key={id} title={title} />
                        ))}
                    </>
                ))}
            </Wrapper>
        </Container>
    </>
);

export default Backlog;
