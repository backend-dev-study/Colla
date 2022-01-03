import React, { useState } from 'react';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import UserIcon from '../../components/Icon/User';
import UserModal from '../../components/Modal/User';
import { Container, HomeImage, Wrapper } from './style';

const Home = () => {
    const [modalOnOff, setModalOnOff] = useState(true);

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
            </Container>
        </>
    );
};

export default Home;
