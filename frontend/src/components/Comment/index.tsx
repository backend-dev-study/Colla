import React, { ChangeEvent, FC, useState } from 'react';
import { modifyComment } from '../../apis/comment';
import { useUserState } from '../../stores/userState';
import { CommentType } from '../../types/comment';
import { ButtonContainer, Container, Contents, ContentsInput, ContentsInputContainer, Writer } from './style';

interface PropType {
    comment: CommentType;
    onClickInputSubComment: Function;
}

export const Comment: FC<PropType> = ({ comment, onClickInputSubComment }) => {
    const profile = useUserState();
    const { id, writer, contents } = comment;

    const [currentContents, setCurrentContents] = useState<string>(contents);
    const [modifyingContents, setModifyingContents] = useState<string>(contents);
    const [modifyCommentInput, setModifyCommentInput] = useState<boolean>(false);

    const handleModifyContents = (event: ChangeEvent) => {
        setModifyingContents((event.target as HTMLInputElement).value);
    };

    const handleModifyComment = () => {
        setModifyingContents(currentContents);
        setModifyCommentInput((prev) => !prev);
    };

    const handleModifyButton = async () => {
        const res = await modifyComment(comment.id, modifyingContents);
        setCurrentContents(res.data.contents);
        setModifyingContents(res.data.contents);
        setModifyCommentInput((prev) => !prev);
    };

    return (
        <Container>
            <Writer image={writer.avatar} />
            <Contents>
                <span>{writer.displayName}</span>
                <ContentsInputContainer>
                    <ContentsInput
                        className={modifyCommentInput ? 'modifying' : ''}
                        value={modifyingContents}
                        onChange={handleModifyContents}
                    />
                    {modifyCommentInput ? (
                        <>
                            <div onClick={handleModifyButton}>완료</div>
                            <div onClick={handleModifyComment}>취소</div>
                        </>
                    ) : null}
                </ContentsInputContainer>
                <ButtonContainer>
                    {writer.githubId === profile.contents.githubId ? (
                        <>
                            <div onClick={handleModifyComment}>수정</div>
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
