import React, { useState } from 'react';

import { Wrapper, SearchInput, SearchList, Place } from './style';

const PlaceModal = () => {
    const [input, setInput] = useState('');

    const handleInput = (e: any) => setInput(e.value);

    return (
        <Wrapper>
            <SearchInput value={input} onChange={handleInput} />
            <SearchList>
                <Place>장소1</Place>
                <Place>장소2</Place>
                <Place>장소3</Place>
            </SearchList>
        </Wrapper>
    );
};

export default PlaceModal;
