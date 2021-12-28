import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    userName: string;
    image: string;
}

const UserIcon: FC<PropType> = ({ userName, image }) =>
    image === '' ? <Container>{userName[0].toUpperCase()}</Container> : <ImageContainer image={image} />;

export default UserIcon;
