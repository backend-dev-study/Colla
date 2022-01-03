import React, { useState } from 'react';

// import MainImageSrc from '../../../public/assets/images/main-image.jpg';
// import ProjectIcon from '../../components/ProjectIcon';
import Header from '../../components/Header';
import UserIcon from '../../components/Icon/User';
import DetailUserModal from '../../components/Modal/DetailUser';
import UserModal from '../../components/Modal/User';
import { Container, Wrapper } from './style';

const Home = () => {
    const [modalOnOff, setModalOnOff] = useState(false);
    const [detailModalOnOff, setDetailModalOnOff] = useState(false);
    const handleModal = () => (modalOnOff ? setModalOnOff(false) : setModalOnOff(true));
    const handleDetailModal = () => (detailModalOnOff ? setDetailModalOnOff(false) : setDetailModalOnOff(true));
    return (
        <>
            <Header />
            <Container>
                <Wrapper>
                    <div onClick={handleModal}>
                        <UserIcon userName={'Maxcha'} image={''} size={'small'} />
                    </div>
                    {modalOnOff && <UserModal userName={'Maxcha'} id={'123'} onClick={handleDetailModal} />}
                </Wrapper>
            </Container>
            {detailModalOnOff && <DetailUserModal userName={'max'} image={''} />}
        </>
    );
};

export default Home;
