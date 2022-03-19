import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import { getMeetingPlaces } from '../../apis/meeting-place';
import Header from '../../components/Header';
import Map from '../../components/Map';
import PlaceModal from '../../components/Modal/Place';
import Place from '../../components/Place';
import SideBar from '../../components/SideBar';
import useModal from '../../hooks/useModal';
import { MeetingPlaceType } from '../../types/meeting-place';
import { StateType } from '../../types/project';
import { Container, Wrapper, AddPlace, PlaceList, PlaceContainer } from './style';

const MeetingPlace = () => {
    const { state } = useLocation<StateType>();
    const { Modal, setModal } = useModal();
    const [meetingPlaces, setMeetingPlaces] = useState<Array<MeetingPlaceType>>([]);
    const [specificMeetingPlaces, setSpecificMeetingPlaces] = useState<Array<MeetingPlaceType>>([]);

    const updateMeetingPlaces = async (projectId: number) => {
        const response = await getMeetingPlaces(projectId);
        setMeetingPlaces(response.data);
        setSpecificMeetingPlaces(response.data);
    };

    useEffect(() => {
        updateMeetingPlaces(state.projectId);
    }, []);

    return (
        <>
            <Header />
            <SideBar />
            <Container>
                <Wrapper>
                    <PlaceContainer>
                        <AddPlace onClick={setModal}>모임 장소 추가</AddPlace>
                        <PlaceList>
                            {specificMeetingPlaces.map((place: MeetingPlaceType, idx: number) => (
                                <Place meetingPlace key={idx} info={place} />
                            ))}
                        </PlaceList>
                    </PlaceContainer>
                    <Modal>
                        <PlaceModal updatePlaces={updateMeetingPlaces} />
                    </Modal>
                    <Map meetingPlaces={meetingPlaces} setSpecificMeetingPlaces={setSpecificMeetingPlaces} />
                </Wrapper>
            </Container>
        </>
    );
};

export default MeetingPlace;
