import React from 'react';
import { useRecoilValue } from 'recoil';

import { projectInfoState } from '../../stores/projectState';
import UserIcon from '../Icon/User';
import { Container, LeftNav, ProjectDesc, ProjectInfo, ProjectTitle, RightNav } from './style';

const Header = () => {
    const projectInfo = useRecoilValue(projectInfoState);
    return (
        <Container>
            <LeftNav>
                <ProjectInfo>
                    <ProjectTitle>{projectInfo.name}</ProjectTitle>
                    <ProjectDesc>{projectInfo.desc}</ProjectDesc>
                </ProjectInfo>
            </LeftNav>
            <RightNav>
                <UserIcon size="small" />
            </RightNav>
        </Container>
    );
};

export default Header;
