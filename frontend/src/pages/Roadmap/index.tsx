import React from 'react';

import Header from '../../components/Header';
import RoadmapStory from '../../components/RoadmapStory';
import { SideBar } from '../../components/SideBar';
import { StoryType } from '../../types/roadmap';
import { Container, RoadmapArea, Wrapper } from './style';

const dummyData: StoryType[] = [
    {
        title: 'before start story',
        start: '2022/02/08',
        end: '2022/03/12',
    },
    { title: 'after end story', start: '2022/03/23', end: '2022/05/06' },
    { title: 'short story', start: '2022/03/11', end: '2022/03/13' },
    { title: 'long story', start: '2022/03/15', end: '2022/04/18' },
    { title: 'after start story', start: '2022/05/04', end: '2022/06/18' },
    { title: 'before end story', start: '2022/02/04', end: '2022/02/18' },
];

const Roadmap = () => (
    <>
        <Header />
        <SideBar />
        <Container>
            <Wrapper>
                <RoadmapArea>
                    {dummyData.map(({ start, end, title }, index) =>
                        start && end ? <RoadmapStory key={index} title={title} start={start} end={end} /> : null,
                    )}
                </RoadmapArea>
            </Wrapper>
        </Container>
    </>
);

export default Roadmap;
