import React from 'react';

import Header from '../../components/Header';
import { Map } from '../../components/Map';
import PlaceModal from '../../components/Modal/Place';
import { SideBar } from '../../components/SideBar';
import useModal from '../../hooks/useModal';
import { Container, Wrapper, AddPlace, Place, PlaceList, PlaceContainer } from './style';

const MeetingPlace = () => {
    const { Modal, setModal } = useModal();

    return (
        <>
            <Header />
            <SideBar />
            <Container>
                <Wrapper>
                    <PlaceContainer>
                        <AddPlace onClick={setModal}>모임 장소 추가</AddPlace>
                        <PlaceList>
                            <Place>장소1</Place>
                            <Place>장소2</Place>
                            <Place>장소3</Place>
                            <Place>장소3</Place>
                            <Place>장소3</Place>
                        </PlaceList>
                    </PlaceContainer>
                    <Modal>
                        <PlaceModal />
                    </Modal>
                    <Map />
                </Wrapper>
            </Container>
        </>
    );
};

export default MeetingPlace;
