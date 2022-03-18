import React, { FC, useEffect, useState } from 'react';

import useGPSLocation from '../../../hooks/useGPSLocation';
import { LatLngType, SearchPlaceType } from '../../../types/meeting-place';
import Place from '../../Place';
import { Wrapper, SearchInput, SearchList } from './style';

interface KakaoPlaceType {
    place_name: string;
    place_url: string;
    address_name: string;
    road_address_name: string;
    x: number;
    y: number;
}

interface PropType {
    updatePlaces: Function;
}

const LOADING = '위치 정보를 받아오고 있습니다..';

const PlaceModal: FC<PropType> = ({ updatePlaces }) => {
    const [keyword, setKeyword] = useState('');
    const [places, setPlaces] = useState<SearchPlaceType[]>([]);
    const [location, setLocation] = useState<LatLngType | undefined>(undefined);

    const getLocation = async () => {
        const gps: any = await useGPSLocation();
        setLocation(gps);
    };

    const handleInput = (e: any) => setKeyword(e.target.value);

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key !== 'Enter') return;
        searchKeyword();
    };

    const ps = new kakao.maps.services.Places();

    const searchKeyword = async () => {
        if (keyword === '') return;

        const options = location?.lng ? { location: new kakao.maps.LatLng(location.lat, location.lng) } : {};
        ps.keywordSearch(keyword, handleSearchResult, options);
    };

    const handleSearchResult = (result: KakaoPlaceType[], status: any) => {
        if (status !== kakao.maps.services.Status.OK) return;

        setPlaces(
            result.map(
                ({ place_name: name, road_address_name: address, x: longitude, y: latitude }) =>
                    ({
                        name,
                        address,
                        longitude,
                        latitude,
                    } as SearchPlaceType),
            ),
        );
    };

    useEffect(() => {
        getLocation();
    }, []);

    return (
        <Wrapper>
            <SearchInput value={keyword} onChange={handleInput} onKeyPress={handleKeyPress} disabled={!location} />
            <SearchList>
                {!location
                    ? LOADING
                    : places.map((place, idx) => <Place key={idx} info={place} updatePlaces={updatePlaces} />)}
            </SearchList>
        </Wrapper>
    );
};

export default PlaceModal;
