import React from 'react';
import { useRecoilValue } from 'recoil';

import useModal from '../../hooks/useModal';
import { projectInfoState } from '../../stores/projectState';
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
                <UserIcon size="small" />
            </RightNav>
        </Container>
    );
};

export default Header;
