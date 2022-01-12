import React from 'react';
import { useRecoilValue } from 'recoil';

import { projectInfoState } from '../../stores/atom';
import UserIcon from '../Icon/User';
import { Container, LeftNav, ProjectDesc, ProjectInfo, ProjectManageButton, ProjectTitle, RightNav } from './style';

const userName = 'user';
const userProfileImg = undefined;
const userGithubId = 'user_github_id';

const Header = () => {
    const projectInfo = useRecoilValue(projectInfoState);
    return (
        <Container>
            <LeftNav>
                <ProjectInfo>
                    <ProjectTitle>{projectInfo.name}</ProjectTitle>
                    <ProjectDesc>{projectInfo.desc}</ProjectDesc>
                </ProjectInfo>
                {projectInfo.name ? <ProjectManageButton>관리</ProjectManageButton> : null}
            </LeftNav>
            <RightNav>
                <UserIcon userName={userName} githubId={userGithubId} image={userProfileImg} size="small" />
            </RightNav>
        </Container>
    );
};

export default Header;
