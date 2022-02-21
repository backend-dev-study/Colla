import React, { FC } from 'react';

import starImg from '../../../public/assets/images/star.png';
import { Attributes, Manager, Priority, Star, Tag, Tags, Wrapper } from './style';

interface PropType {
    story?: boolean;
    title: string;
    priority?: number;
    manager?: string;
    tags?: Array<string>;
}

const Issue: FC<PropType> = ({ story, title, priority, manager, tags }) => (
    <>
        <Wrapper story={story}>
            {title}
            {!story ? (
                <Attributes>
                    <Tags>
                        {tags!.map((tag, idx) => (
                            <Tag key={idx}>{tag}</Tag>
                        ))}
                    </Tags>
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
