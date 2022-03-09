import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import { getProjectStories } from '../../apis/story';
import Header from '../../components/Header';
import { IssueList } from '../../components/List/IssueList';
import RoadmapStory from '../../components/RoadmapStory';
import { SideBar } from '../../components/SideBar';
import { StateType } from '../../types/project';
import { StoryType } from '../../types/roadmap';
import { Container, Wrapper, RoadmapArea } from './style';

const Roadmap = () => {
    const { state } = useLocation<StateType>();
    const [storyList, setStoryList] = useState<Array<StoryType>>([]);

    useEffect(() => {
        (async () => {
            const res = await getProjectStories(state.projectId);
            setStoryList(res.data);
        })();
    }, []);

    return (
        <>
            <Header />
            <SideBar />
            <Container>
                <Wrapper>
                    <RoadmapArea>
                        {storyList.map(({ startAt, endAt, title }, index) =>
                            startAt && endAt ? (
                                <RoadmapStory key={index} title={title} start={startAt} end={endAt} />
                            ) : null,
                        )}
                    </RoadmapArea>
                    <IssueList storyList={storyList} />
                </Wrapper>
            </Container>
        </>
    );
};

export default Roadmap;
