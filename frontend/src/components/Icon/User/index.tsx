import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    userName: string;
    size: 'big' | 'small';
    image?: string;
}

const UserIcon: FC<PropType> = ({ userName, image, size }) =>
    image === undefined ? (
        <Container size={size}>{userName[0].toUpperCase()}</Container>
    ) : (
        <ImageContainer image={image} size={size} />
    );
export default UserIcon;
