import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    userName: string;
    image: string;
    size: 'big' | 'small';
    handleModal: () => void;
}

const UserIcon: FC<PropType> = ({ image, size, handleModal }) =>
    image === '' ? (
        <Container size={size} image={''} onClick={handleModal}>
            {'Empty'}
        </Container>
    ) : (
        <ImageContainer image={image} size={size} onClick={handleModal} />
    );

export default UserIcon;
