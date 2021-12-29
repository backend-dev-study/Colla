import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    userName: string;
    image: string;
    size: 'big' | 'small';
}

const UserIcon: FC<PropType> = ({ userName, image, size }) =>
    image === '' ? (
        <Container size={size} image={''}>
            {userName[0].toUpperCase()}
        </Container>
    ) : (
        <ImageContainer image={image} size={size} />
    );
export default UserIcon;
