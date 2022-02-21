import React, { FC } from 'react';

import starImg from '../../../public/assets/images/star.png';
import { Attributes, Manager, Priority, Star, Wrapper } from './style';

interface PropType {
    story?: boolean;
    title: string;
    priority?: number;
    manager?: string;
}

const Issue: FC<PropType> = ({ story, title, priority, manager }) => (
    <>
        <Wrapper story={story}>
            {title}
            {!story ? (
                <Attributes>
                    <Priority>
                        {Array.from({ length: priority! }, (v, i) => i).map((v, i) => (
                            <Star key={i} src={starImg} />
                        ))}
                    </Priority>
                    <Manager src={manager} />
                </Attributes>
            ) : null}
        </Wrapper>
    </>
);

export default Issue;
