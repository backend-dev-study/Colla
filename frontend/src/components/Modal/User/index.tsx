import React, { FC, useState } from 'react';

import edit from '../../../../public/assets/images/edit.svg';
import { Container, Wrapper, DisplayName, GithubId, LogoutButton, Edit, InputBar, UserName } from './style';

interface PropType {
    userName: string;
    id: string;
}

const UserModal: FC<PropType> = ({ userName, id }) => {
    const [showInputBar, setShowInputBar] = useState(false);

    const handleModifyName = () => {
        showInputBar ? setShowInputBar(false) : setShowInputBar(true);
    };

    return (
        <Container>
            {id ? (
                <>
                    <Wrapper>
                        <DisplayName>display name : </DisplayName>
                        {showInputBar ? <InputBar value={userName} /> : <UserName>{userName}</UserName>}
                        <Edit src={edit} onClick={handleModifyName} />
                    </Wrapper>
                    <GithubId>github id : {id}</GithubId>
                    <LogoutButton>로그아웃</LogoutButton>
                </>
            ) : (
                <LogoutButton>로그인</LogoutButton>
            )}
        </Container>
    );
};

export default UserModal;
