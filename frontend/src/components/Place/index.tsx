import React, { FC } from 'react';
import { Wrapper } from './style';

interface PropType {
    name: string;
    address: string;
    image?: string;
}

const Place: FC<PropType> = ({ name, address, image }) => (
    <Wrapper>
        {image ? <img src={image} /> : null}
        <div>{name}</div>
        <div>{address}</div>
    </Wrapper>
);

export default Place;
