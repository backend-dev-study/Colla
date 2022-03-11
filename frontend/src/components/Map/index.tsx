import React, { useEffect } from 'react';

import { MeetingPlaceType } from '../../types/meeting-place';
import { InfoWindow } from './InfoWindow';
import { MapContainer } from './style';

const dummy: Array<MeetingPlaceType> = [
    {
        id: 1,
        name: '컴포즈커피',
        image: null,
        longitude: 127.03187208216006,
        latitude: 37.4918120829107,
        address: '서울특별시 강남구 역삼동 강남대로66길 14',
    },
    {
        id: 2,
        name: '달커피',
        image: null,
        longitude: 127.03173245188478,
        latitude: 37.49074981117741,
        address: '서울특별시 강남구 역삼동 강남대로 308 1층',
    },
    {
        id: 3,
        name: '커피빈',
        image: null,
        longitude: 127.03229097297763,
        latitude: 37.48765414782675,
        address: '서울특별시 서초구 서초동 1361-9',
    },
];

export const Map = () => {
    let markers: Array<any> = [];
    let infoWindows: Array<any> = [];

    const handleMarkerClick = (map: any, marker: any) => {
        const idx = markers.indexOf(marker);
        handleClickOtherArea();
        infoWindows[idx].open(map, marker);
    };

    const handleClickOtherArea = () => {
        infoWindows.forEach((infoWindow) => infoWindow.close());
    };

    const markMeetingPlace = (map: any) => {
        markers = dummy.map(({ latitude, longitude }) => {
            const marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(latitude, longitude),
                map,
            });

            naver.maps.Event.addListener(marker, 'click', () => handleMarkerClick(map, marker));
            return marker;
        });
    };

    const createInfoWindows = () => {
        infoWindows = dummy.map(
            (meetingPlace, idx) =>
                new naver.maps.InfoWindow({
                    position: markers[idx],
                    content: InfoWindow(meetingPlace),
                    borderWidth: 0,
                }),
        );
    };

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

        const map = new naver.maps.Map('map', mapOptions);
        markMeetingPlace(map);
        createInfoWindows();
        naver.maps.Event.addListener(map, 'click', () => handleClickOtherArea());
    }, []);

    return (
        <MapContainer>
            <div id="map" />
        </MapContainer>
    );
};
