import React, { useState } from 'react';

import { Container, Weight } from './style';

export const Priority = () => {
    const [priority, setPriority] = useState(-1);

    const changePriority = (idx: number) => {
        setPriority(idx);
    };

    return (
        <Container>
            {Array(5)
                .fill(0)
                .map((el, i) => i + 1)
                .map((el, idx) => (
                    <Weight
                        key={idx}
                        className={priority === idx ? 'selected' : ''}
                        onClick={() => changePriority(idx)}
                    >
                        {el}
                    </Weight>
                ))}
        </Container>
    );
};
