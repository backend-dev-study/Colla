import React, { ChangeEvent, FC, useState } from 'react';
import { deleteComment, modifyComment } from '../../apis/comment';
import { useUserState } from '../../stores/userState';
import { CommentType } from '../../types/comment';
import { ButtonContainer, Container, Contents, ContentsInput, ContentsInputContainer, Writer } from './style';

interface PropType {
    subComment?: boolean;
    comment: CommentType;
    onClickInputSubComment: Function;
    setCommentList: Function;
}

export const Comment: FC<PropType> = ({ subComment, comment, onClickInputSubComment, setCommentList }) => {
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
        const res = await modifyComment(id, modifyingContents);
        setCurrentContents(res.data.contents);
        setModifyingContents(res.data.contents);
        setModifyCommentInput((prev) => !prev);
    };

    const handleDeleteButton = async () => {
        await deleteComment(id);
        subComment
            ? setCommentList((prev: Array<CommentType>) => {
                  const newCommentList = [...prev];
                  newCommentList.forEach((el) => {
                      el.subComments.forEach((subEl, idx) => {
                          if (subEl.id === id) el.subComments.splice(idx, 1);
                      });
                  });
                  return newCommentList;
              })
            : setCommentList((prev: Array<CommentType>) => [...prev].filter((el) => el.id !== id));
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
                            <div onClick={handleModifyButton}>??????</div>
                            <div onClick={handleModifyComment}>??????</div>
                        </>
                    ) : null}
                </ContentsInputContainer>
                <ButtonContainer>
                    {writer.githubId === profile.contents.githubId ? (
                        <>
                            <div onClick={handleModifyComment}>??????</div>
                            <div onClick={handleDeleteButton}>??????</div>
                        </>
                    ) : (
                        <div onClick={() => onClickInputSubComment(id)}>??????</div>
                    )}
                </ButtonContainer>
            </Contents>
        </Container>
    );
};
