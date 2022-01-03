import React from 'react';

import { Container, DisplayName, Description, LogOutButton } from './style';

const ProjectModal = () => (
    <Container>
        <div />
        <DisplayName placeholder={'프로젝트 이름'} />
        <Description placeholder={'프로젝트 설명'} />
        <LogOutButton>로그아웃</LogOutButton>
    </Container>
);
export default ProjectModal;
