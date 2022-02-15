import React from 'react';

import { Comment, CommentInput, Container, SaveButton, Title } from './style';

export const CommentContainer = () => (
    <Container>
        <Title>댓글</Title>
        <Comment>
            <CommentInput />
            <SaveButton>등록</SaveButton>
        </Comment>
    </Container>
);
