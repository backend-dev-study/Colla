import React, { FC, useEffect } from 'react';

import { useLocation } from 'react-router-dom';
import { getSpecificAreaMeetingPlace } from '../../apis/meeting-place';
import { MeetingPlaceType } from '../../types/meeting-place';
import { StateType } from '../../types/project';
import { InfoWindow } from './InfoWindow';
import { MapContainer } from './style';

const Map: FC<PropType> = ({ meetingPlaces, setSpecificMeetingPlaces }) => {
    let markers: Array<any> = [];
    let infoWindows: Array<any> = [];
    let timer: NodeJS.Timeout;
    const { state } = useLocation<StateType>();

    const handleMarkerClick = (map: any, marker: any) => {
        const idx = markers.indexOf(marker);
        handleClickOtherArea();
        infoWindows[idx].open(map, marker);
    };

    const handleClickOtherArea = () => {
        infoWindows.forEach((infoWindow) => infoWindow.close());
    };

    const markMeetingPlace = (map: any) => {
        markers = meetingPlaces.map(({ latitude, longitude }) => {
            const marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(latitude, longitude),
                map,
            });

            naver.maps.Event.addListener(marker, 'click', () => handleMarkerClick(map, marker));
            return marker;
        });
    };

    const createInfoWindows = () => {
        infoWindows = meetingPlaces.map(
            (meetingPlace, idx) =>
                new naver.maps.InfoWindow({
                    position: markers[idx],
                    content: InfoWindow(meetingPlace),
                    borderWidth: 0,
                }),
        );
    };

    const searchSpecificAreaMeetingPlace = (map: any) => {
        if (timer) {
            clearTimeout(timer);
        }

        timer = setTimeout(async () => {
            const { _max, _min } = map.getBounds();
            const res = await getSpecificAreaMeetingPlace(state.projectId, _min._lng, _max._lng, _min._lat, _max._lat);
            setSpecificMeetingPlaces(res.data);
        }, 500);
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
        naver.maps.Event.addListener(map, 'drag', () => searchSpecificAreaMeetingPlace(map));
    }, [meetingPlaces]);

    return (
        <MapContainer>
            <div id="map" />
        </MapContainer>
    );
};

export default Map;
