import React, { useState } from 'react';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import UserIcon from '../../components/Icon/User';
import UserModal from '../../components/Modal/User';
import { Container, HomeImage, ProjectNotice, Wrapper } from './style';

const Home = () => {
    const [modalOnOff, setModalOnOff] = useState(false);

    const handleModal = () => {
        modalOnOff ? setModalOnOff(false) : setModalOnOff(true);
    };

    return (
        <>
            <Wrapper>
                <UserIcon userName={'Maxcha'} image={''} size={'small'} handleModal={handleModal} />
                {modalOnOff && <UserModal userName={'Maxcha'} id={'123'} />}
            </Wrapper>
            <Container>
                <HomeImage src={HomeImageSrc} />
                <ProjectNotice>프로젝트를 추가해보세요!</ProjectNotice>
            </Container>
        </>
    );
};

export default Home;
