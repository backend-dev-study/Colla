import React, { useState } from 'react';

import { SearchPlaceType } from '../../../types/meeting-place';
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

const PlaceModal = () => {
    const [keyword, setKeyword] = useState('');
    const [places, setPlaces] = useState<SearchPlaceType[]>([]);

    const handleInput = (e: any) => setKeyword(e.target.value);

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key !== 'Enter') return;
        searchKeyword();
    };

    const ps = new kakao.maps.services.Places();

    const searchKeyword = () => {
        console.log(keyword);
        if (keyword === '') return;

        ps.keywordSearch(keyword, handleSearchResult);
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

    return (
        <Wrapper>
            <SearchInput value={keyword} onChange={handleInput} onKeyPress={handleKeyPress} />
            <SearchList>
                {places.map((place, idx) => (
                    <Place key={idx} info={place} />
                ))}
            </SearchList>
        </Wrapper>
    );
};

export default PlaceModal;
