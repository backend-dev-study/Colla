import React from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import Header from '../../components/Header';
import KanbanCol from '../../components/KanbanCol';
import { SideBar } from '../../components/SideBar';
import { projectState } from '../../stores/projectState';
import { Wrapper, KanbanAddButton, KanbanAdditional, Container } from './style';

const statuses = ['To Do', 'Progress', 'Done'];

const Kanban = () => {
    const history = useHistory();
    const menu = ['로드맵', '백로그', '대시보드', '지도'];
    const project = useRecoilValue(projectState);

    if (!project.id) {
        history.push('/home');
    }

    return (
        <>
            <Header />
            <SideBar props={menu} />
            <Container>
                <Wrapper>
                    {statuses.map((value) => (
                        <KanbanCol key={value} status={value} />
                    ))}
                    <KanbanAdditional>
                        <KanbanAddButton />
                    </KanbanAdditional>
                </Wrapper>
            </Container>
        </>
    );
};

export default Kanban;
