import React, { FC } from 'react';

import { Text, Hover, Wrapper } from './style';

interface PropType {
    text: string;
    hover: string;
}

const TextWithHover: FC<PropType> = ({ text, hover }) => (
    <Wrapper>
        <Text>{text}</Text>
        <Hover>{hover}</Hover>
    </Wrapper>
);
export default TextWithHover;
