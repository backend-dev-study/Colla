import React, { ChangeEvent, FC, useState } from 'react';

import edit from '../../../../public/assets/images/edit.svg';
import { logout } from '../../../apis/auth';
import { updateDisplayName } from '../../../apis/user';
import {
    Container,
    Wrapper,
    DisplayName,
    GithubId,
    LogoutButton,
    Edit,
    InputBar,
    UserName,
    Complete,
    Cancel,
} from './style';

interface PropType {
    userName: string;
    githubId: string;
}

const UserModal: FC<PropType> = ({ userName, githubId }) => {
    const [displayName, setDisplayName] = useState(userName);
    const [modifyingName, setModifyingName] = useState(userName);
    const [showInputBar, setShowInputBar] = useState(false);

    const preventClose = (event: any) => event.stopPropagation();

    const handleEditIcon = () => {
        showInputBar ? setShowInputBar(false) : setShowInputBar(true);
        setModifyingName(displayName);
    };

    const handleModifyingName = (event: ChangeEvent) => {
        setModifyingName((event.target as HTMLInputElement).value);
    };

    const handleCompleteButton = async () => {
        await updateDisplayName(modifyingName);
        setDisplayName(modifyingName);
        setShowInputBar(false);
    };

    const handleLogoutButton = async () => {
        await logout();
        window.location.href = '/';
    };

    return (
        <Container onClick={preventClose}>
            <Wrapper>
                <DisplayName>display name : </DisplayName>
                {showInputBar ? (
                    <InputBar value={modifyingName} onChange={handleModifyingName} />
                ) : (
                    <UserName>{displayName}</UserName>
                )}
                {showInputBar ? (
                    <>
                        <Complete onClick={handleCompleteButton}>완료</Complete>
                        <Cancel onClick={handleEditIcon}>취소</Cancel>
                    </>
                ) : (
                    <Edit src={edit} onClick={handleEditIcon} />
                )}
            </Wrapper>
            <GithubId>github id : {githubId}</GithubId>
            <LogoutButton onClick={handleLogoutButton}>로그아웃</LogoutButton>
        </Container>
    );
};

export default UserModal;
