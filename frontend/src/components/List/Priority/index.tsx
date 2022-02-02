import React, { FC } from 'react';

import { Container, Weight } from './style';

interface PropType {
    priority: number;
    handleChangePriority: Function;
}

export const Priority: FC<PropType> = ({ priority, handleChangePriority }) => (
    <Container>
        {Array(5)
            .fill(0)
            .map((el, i) => i + 1)
            .map((el, idx) => (
                <Weight
                    key={idx}
                    className={priority === idx ? 'selected' : ''}
                    onClick={() => handleChangePriority(idx)}
                >
                    {el}
                </Weight>
            ))}
    </Container>
);
