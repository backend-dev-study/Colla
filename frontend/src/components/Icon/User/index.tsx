import React, { FC, useState } from 'react';

import UserModal from '../../Modal/User';
import { Container, Icon, ImageContainer } from './style';

interface PropType {
    userName: string;
    githubId: string;
    size: 'big' | 'small';
    image?: string;
}

const UserIcon: FC<PropType> = ({ userName, githubId, image, size }) => {
    const [modal, setModal] = useState(false);

    const handleModal = () => setModal(!modal);

    return (
        <>
            <Icon onClick={handleModal}>
                {image ? (
                    <ImageContainer image={image} size={size} />
                ) : (
                    <Container size={size}>{userName[0].toUpperCase()}</Container>
                )}
                {modal ? <UserModal userName={userName} id={githubId} /> : null}
            </Icon>
        </>
    );
};
export default UserIcon;
