import React, { FC } from 'react';

import useModal from '../../../hooks/useModal';
import { useUserState } from '../../../stores/userState';
import UserModal from '../../Modal/User';
import { Container, Icon, ImageContainer } from './style';

interface PropType {
    size: 'big' | 'small';
}

const UserIcon: FC<PropType> = ({ size }) => {
    const profile = useUserState();
    const { Modal, setModal } = useModal();

    if (profile.state === 'loading') {
        return (
            <Icon>
                <Container size={size}>...</Container>
            </Icon>
        );
    }

    const { displayName, githubId, avatar } = profile.contents;

    return (
        <Icon onClick={setModal}>
            {avatar ? (
                <ImageContainer image={avatar} size={size} />
            ) : (
                <Container size={size}>{displayName[0].toUpperCase()}</Container>
            )}
            <Modal>
                <UserModal userName={displayName} githubId={githubId} />
            </Modal>
        </Icon>
    );
};
export default UserIcon;
