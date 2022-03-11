import React from 'react';

import Template from '../../components/Template';
import { Container, AddPlace, PlaceList, Place } from './style';

const Map = () => (
    <Template>
        <Container>
            <AddPlace>모임 장소 추가</AddPlace>
            <PlaceList>
                <Place>장소1</Place>
                <Place>장소2</Place>
                <Place>장소3</Place>
                <Place>장소3</Place>
                <Place>장소3</Place>
            </PlaceList>
        </Container>
    </Template>
);

export default Map;
