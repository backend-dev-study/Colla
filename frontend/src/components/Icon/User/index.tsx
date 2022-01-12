import React, { FC, useState } from 'react';

import useUserState from '../../../stores/userState';
import UserModal from '../../Modal/User';
import { Container, Icon, ImageContainer } from './style';

interface PropType {
    size: 'big' | 'small';
}

const UserIcon: FC<PropType> = ({ size }) => {
    const [modal, setModal] = useState(false);
    const profile = useUserState();

    const handleModal = () => setModal(!modal);

    if (profile.state === 'loading') {
        return (
            <Icon>
                <Container size={size}>...</Container>
            </Icon>
        );
    }

    const { displayName, githubId, avatar } = profile.contents;

    return (
        <Icon onClick={handleModal}>
            {avatar ? (
                <ImageContainer image={avatar} size={size} />
            ) : (
                <Container size={size}>{displayName[0].toUpperCase()}</Container>
            )}
            {modal ? <UserModal userName={displayName} githubId={githubId} /> : null}
        </Icon>
    );
};
export default UserIcon;
