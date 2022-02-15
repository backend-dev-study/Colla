import React, { FC } from 'react';
import { CommentType } from '../../../types/comment';
import { Container, Contents, SubComment, SuperComment, SuperCommentContainer, Writer } from './style';

interface PropType {
    commentList: Array<CommentType>;
}

export const CommentList: FC<PropType> = ({ commentList }) => (
    <Container>
        {commentList.map(({ id, writer, contents, subComments }) => (
            <SuperCommentContainer key={id}>
                <SuperComment>
                    <Writer image={writer.avatar} />
                    <Contents>
                        <span>{writer.displayName}</span>
                        <span>{contents}</span>
                    </Contents>
                </SuperComment>
                {subComments.map(({ id, writer, contents }) => (
                    <SubComment key={id}>
                        <Writer image={writer.avatar} />
                        <Contents>
                            <span>{writer.displayName}</span>
                            <span>{contents}</span>
                        </Contents>
                    </SubComment>
                ))}
            </SuperCommentContainer>
        ))}
    </Container>
);
