import React, { useEffect } from 'react';

import { MapContainer } from './style';

export const Map = () => {
    useEffect(() => {
        const mapOptions = {
            center: new naver.maps.LatLng(37.511337, 127.012084),
            zoom: 13,
            zoomControl: true,
            zoomControlOptions: {
                style: naver.maps.ZoomControlStyle.SMALL,
                position: naver.maps.Position.TOP_RIGHT,
            },
        };
        new naver.maps.Map('map', mapOptions);
    }, []);

    return (
        <MapContainer>
            <div id="map" />
        </MapContainer>
    );
};
