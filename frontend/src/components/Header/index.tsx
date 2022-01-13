import React from 'react';
import { useRecoilValue } from 'recoil';

import useModal from '../../hooks/useModal';
import { projectInfoState } from '../../stores/atom';
import UserIcon from '../Icon/User';
import InviteModal from '../Modal/Invite';
import {
    Container,
    LeftNav,
    ProjectDesc,
    ProjectInfo,
    ProjectManageButton,
    ProjectManagement,
    ProjectTitle,
    RightNav,
} from './style';

const userName = 'user';
const userProfileImg = undefined;
const userGithubId = 'user_github_id';

const Header = () => {
    const { Modal, setModal } = useModal();
    const projectInfo = useRecoilValue(projectInfoState);
    return (
        <Container>
            <LeftNav>
                <ProjectInfo>
                    <ProjectTitle>{projectInfo.name}</ProjectTitle>
                    <ProjectDesc>{projectInfo.desc}</ProjectDesc>
                </ProjectInfo>
                <ProjectManagement>
                    {projectInfo.name ? <ProjectManageButton onClick={setModal}>관리</ProjectManageButton> : null}
                    <Modal>
                        <InviteModal />
                    </Modal>
                </ProjectManagement>
            </LeftNav>
            <RightNav>
                <UserIcon userName={userName} githubId={userGithubId} image={userProfileImg} size="small" />
            </RightNav>
        </Container>
    );
};

export default Header;
