import React from 'react';
import { useRecoilValue } from 'recoil';

import useModal from '../../hooks/useModal';
import { projectState } from '../../stores/projectState';
import NoticeIcon from '../Icon/Notice';
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
    const project = useRecoilValue(projectState);

    return (
        <Container>
            <LeftNav>
                <ProjectInfo>
                    <ProjectTitle>{project.name}</ProjectTitle>
                    <ProjectDesc>{project.description}</ProjectDesc>
                </ProjectInfo>
                <ProjectManagement>
                    {project.name ? <ProjectManageButton onClick={setModal}>관리</ProjectManageButton> : null}
                    <Modal>
                        <InviteModal />
                    </Modal>
                </ProjectManagement>
            </LeftNav>
            <RightNav>
                <NoticeIcon />
                <UserIcon size="small" />
            </RightNav>
        </Container>
    );
};

export default Header;
