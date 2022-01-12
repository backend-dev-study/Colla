import React, { ChangeEvent, FC, useState } from 'react';

import edit from '../../../../public/assets/images/edit.svg';
import { logout } from '../../../apis/auth';
import { isResponseSuccess } from '../../../apis/common';
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
    id: string;
}

const UserModal: FC<PropType> = ({ userName, id }) => {
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

    const handleCompleteButton = () => {
        setDisplayName(modifyingName);
        setShowInputBar(false);
    };

    const handleLogoutButton = async () => {
        const res = await logout();
        if (isResponseSuccess(res.status)) {
            window.location.href = '/';
        }
    };

    return (
        <Container onClick={preventClose}>
            {id ? (
                <>
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
                    <GithubId>github id : {id}</GithubId>
                    <LogoutButton onClick={handleLogoutButton}>로그아웃</LogoutButton>
                </>
            ) : (
                <LogoutButton>로그인</LogoutButton>
            )}
        </Container>
    );
};

export default UserModal;
