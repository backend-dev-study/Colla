import React, { FC, useEffect, useState } from 'react';
import { ClipLoader } from 'react-spinners';

import useGPSLocation from '../../../hooks/useGPSLocation';
import { LatLngType, SearchPlaceType } from '../../../types/meeting-place';
import Place from '../../Place';
import { Wrapper, SearchInput, SearchList, Loading, NoPlace } from './style';

interface KakaoPlaceType {
    place_name: string;
    place_url: string;
    address_name: string;
    road_address_name: string;
    category_group_name: string;
    x: number;
    y: number;
}

interface PropType {
    updatePlaces: Function;
}

const LOADING = '위치 정보를 받아오고 있습니다..';
const NO_PLACE = '검색된 장소가 없습니다. 추가하고 싶은 장소를 검색하세요!';

const PlaceModal: FC<PropType> = ({ updatePlaces }) => {
    const [keyword, setKeyword] = useState('');
    const [places, setPlaces] = useState<SearchPlaceType[]>([]);
    const [location, setLocation] = useState<LatLngType | undefined>(undefined);

    const getLocation = async () => {
        const gps: any = await useGPSLocation();
        setLocation(gps);
    };

    const handlePlaces = () => {
        if (places.length === 0) return <NoPlace>{NO_PLACE}</NoPlace>;
        return places.map((place, idx) => <Place key={idx} info={place} updatePlaces={updatePlaces} />);
    };

    const handleInput = (e: any) => setKeyword(e.target.value);

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key !== 'Enter') return;
        searchKeyword();
    };

    const searchKeyword = async () => {
        if (keyword === '') return;

        const ps = new kakao.maps.services.Places();
        const options = location?.lng ? { location: new kakao.maps.LatLng(location.lat, location.lng) } : {};
        ps.keywordSearch(keyword, handleSearchResult, options);
    };

    const handleSearchResult = (result: KakaoPlaceType[], status: any) => {
        if (status !== kakao.maps.services.Status.OK) return;

        setPlaces(
            result.map(
                ({
                    place_name: name,
                    road_address_name: address,
                    x: longitude,
                    y: latitude,
                    category_group_name: category,
                }) =>
                    ({
                        name,
                        address,
                        longitude,
                        latitude,
                        category,
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
                {!location ? (
                    <>
                        <Loading>{LOADING}</Loading> <ClipLoader size="60" color="#000" />
                    </>
                ) : (
                    handlePlaces()
                )}
            </SearchList>
        </Wrapper>
    );
};

export default PlaceModal;
