import React, { FC } from 'react';
import { Attributes, Wrapper } from './style';

interface PropType {
    story?: boolean;
    title: string;
}

const Issue: FC<PropType> = ({ story, title }) => (
    <>
        <Wrapper story={story}>
            {title}
            {!story ? (
                <Attributes>
                    <div>우선순위</div>
                    <div>태그들</div>
                    <div>담당자</div>
                </Attributes>
            ) : null}
        </Wrapper>
    </>
);

export default Issue;
