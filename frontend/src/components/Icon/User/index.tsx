import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    userName: string;
    image: string;
    onClick: () => void;
}

const UserIcon: FC<PropType> = ({ userName, image, onClick }) => (
    <div onClick={onClick}>
        {image === '' ? <Container>{userName[0].toUpperCase()}</Container> : <ImageContainer image={image} />}
    </div>
);
export default UserIcon;
