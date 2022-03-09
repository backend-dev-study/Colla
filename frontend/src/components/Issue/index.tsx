import React, { FC } from 'react';

import starImg from '../../../public/assets/images/star.png';
import { Attributes, Manager, Priority, Star, Tag, Tags, Wrapper } from './style';

interface PropType {
    story?: boolean;
    id?: number;
    title: string;
    priority?: number;
    manager?: string;
    tags?: Array<string>;
    status?: string;
    showTaskModal?: Function;
}

const NO_STORY = '스토리에 속해있지 않은 테스크 목록';

const Issue: FC<PropType> = ({ story, id, title, priority, manager, tags, status, showTaskModal }) => (
    <>
        <Wrapper story={story} onClick={story ? () => {} : (event) => showTaskModal!(event, id, status)}>
            {title ? title : NO_STORY}
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
