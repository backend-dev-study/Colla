import React, { FC } from 'react';
import UserIcon from '../../Icon/User';
import { Container, DisplayName, Description, LogOutButton } from './style';

interface PropType {
    userName: string;
    image: string;
}

const DetailUserModal: FC<PropType> = ({ userName, image }) => (
    <Container>
        <UserIcon userName={userName} image={image} size={'big'} />
        <DisplayName placeholder={'이름'} value={userName} />
        <Description placeholder={'설명'} />
        <LogOutButton>로그아웃</LogOutButton>
    </Container>
);
export default DetailUserModal;
