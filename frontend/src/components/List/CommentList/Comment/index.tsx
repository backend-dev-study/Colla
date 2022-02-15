import React, { FC } from 'react';
import { useUserState } from '../../../../stores/userState';
import { CommentType } from '../../../../types/comment';
import { ButtonContainer, Container, Contents, Writer } from './style';

interface PropType {
    comment: CommentType;
    onClickInputSubComment: Function;
}

export const Comment: FC<PropType> = ({ comment, onClickInputSubComment }) => {
    const profile = useUserState();
    const { id, writer, contents } = comment;

    return (
        <Container>
            <Writer image={writer.avatar} />
            <Contents>
                <span>{writer.displayName}</span>
                <span>{contents}</span>
                <ButtonContainer>
                    {writer.githubId === profile.contents.githubId ? (
                        <>
                            <div>수정</div>
                            <div>삭제</div>
                        </>
                    ) : (
                        <div onClick={() => onClickInputSubComment(id)}>댓글</div>
                    )}
                </ButtonContainer>
            </Contents>
        </Container>
    );
};
