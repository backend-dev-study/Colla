import React, { FC } from 'react';

import useModal from '../../../hooks/useModal';
import UserModal from '../../Modal/User';
import { Container, Icon, ImageContainer } from './style';

interface PropType {
    userName: string;
    githubId: string;
    size: 'big' | 'small';
    image?: string;
}

const UserIcon: FC<PropType> = ({ userName, githubId, image, size }) => {
    const { Modal, setModal } = useModal();

    return (
        <>
            <Icon onClick={setModal}>
                {image ? (
                    <ImageContainer image={image} size={size} />
                ) : (
                    <Container size={size}>{userName[0].toUpperCase()}</Container>
                )}
                <Modal>
                    <UserModal userName={userName} id={githubId} />{' '}
                </Modal>
            </Icon>
        </>
    );
};
export default UserIcon;
