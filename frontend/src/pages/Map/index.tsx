import React from 'react';

import PlaceModal from '../../components/Modal/Place';
import Template from '../../components/Template';
import useModal from '../../hooks/useModal';
import { Container, AddPlace, PlaceList, Place } from './style';

const Map = () => {
    const { Modal, setModal } = useModal();

    return (
        <Template>
            <Container>
                <AddPlace onClick={setModal}>모임 장소 추가</AddPlace>
                <PlaceList>
                    <Place>장소1</Place>
                    <Place>장소2</Place>
                    <Place>장소3</Place>
                    <Place>장소3</Place>
                    <Place>장소3</Place>
                </PlaceList>
            </Container>
            <Modal>
                <PlaceModal />
            </Modal>
        </Template>
    );
};

export default Map;
