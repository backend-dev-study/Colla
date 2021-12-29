import React, { useState } from 'react';

// import MainImageSrc from '../../../public/assets/images/main-image.jpg';
// import ProjectIcon from '../../components/ProjectIcon';
import UserIcon from '../../components/Icon/User';
import UserModal from '../../components/Modal/User';
import { Container, Wrapper } from './style';

const Home = () => {
    const [modalOnOff, setModalOnOff] = useState(false);
    const handleModal = () => (modalOnOff ? setModalOnOff(false) : setModalOnOff(true));
    return (
        <>
            <Container>
                <Wrapper>
                    <UserIcon userName={'Maxcha'} image={''} onClick={handleModal} />
                    {modalOnOff && <UserModal userName={'Maxcha'} id={'123'} />}
                </Wrapper>
            </Container>
        </>
    );
};

export default Home;
