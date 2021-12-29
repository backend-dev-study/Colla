import React, { FC } from 'react';
import edit from '../../../assets/edit.svg';
import { Container, Wrapper, DisplayName, GithubId, LogOutButton, Edit } from './style';

interface PropType {
    userName: string;
    id: string;
}

const UserModal: FC<PropType> = ({ userName, id }) => (
    <Container>
        {id ? (
            <>
                <Wrapper>
                    <DisplayName>display name : {userName}</DisplayName>
                    <Edit src={edit} />
                </Wrapper>
                <GithubId>github id : {id}</GithubId>
                <LogOutButton>로그아웃</LogOutButton>
            </>
        ) : (
            <LogOutButton>로그인</LogOutButton>
        )}
    </Container>
);
export default UserModal;
