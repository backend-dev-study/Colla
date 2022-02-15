import React, { ChangeEvent, FC, useState } from 'react';
import { saveComment } from '../../../apis/comment';
import { CommentType } from '../../../types/comment';
import { SaveButton } from '../../Modal/Task/Comment/style';
import { Comment } from './Comment';
import {
    CancelButton,
    Container,
    SubComment,
    SubCommentContainer,
    SubCommentInput,
    SubCommentInputContainer,
    SuperComment,
    SuperCommentContainer,
} from './style';

interface PropType {
    taskId: number;
    commentList: Array<CommentType>;
    setCommentList: Function;
}

export const CommentList: FC<PropType> = ({ taskId, commentList, setCommentList }) => {
    const [subComment, setSubComment] = useState<string>('');
    const [inputSubComment, setInputSubComment] = useState<number>(-1);

    const handleChangeSubComment = (event: ChangeEvent) => {
        setSubComment((event.target as HTMLInputElement).value);
    };

    const onClickInputSubComment = (commentId: number) => {
        setInputSubComment(commentId);
    };

    const handleSaveButton = async (superCommentId: number) => {
        const res = await saveComment(taskId, subComment, superCommentId);
        setCommentList((prev: Array<CommentType>) => {
            const { id, writer, superCommentId, contents } = res.data;
            const newCommentList = [...prev];

            newCommentList
                .filter((comment) => comment.id === superCommentId)
                .forEach((comment) => comment.subComments.push({ id, writer, contents, subComments: [] }));

            return newCommentList;
        });
        handleCancelButton();
    };

    const handleCancelButton = () => {
        setSubComment('');
        setInputSubComment(-1);
    };

    return (
        <Container>
            {commentList.map((comment, idx) => (
                <SuperCommentContainer key={idx}>
                    <SuperComment>
                        <Comment comment={comment} onClickInputSubComment={onClickInputSubComment} />
                    </SuperComment>
                    <SubCommentContainer>
                        {comment.subComments.map((subComment, idx) => (
                            <SubComment key={idx}>
                                <Comment comment={subComment} onClickInputSubComment={onClickInputSubComment} />
                            </SubComment>
                        ))}
                        {inputSubComment === comment.id ? (
                            <SubCommentInputContainer>
                                <SubCommentInput value={subComment} onChange={handleChangeSubComment} />
                                <SaveButton onClick={() => handleSaveButton(comment.id)}>등록</SaveButton>
                                <CancelButton onClick={handleCancelButton}>취소</CancelButton>
                            </SubCommentInputContainer>
                        ) : null}
                    </SubCommentContainer>
                </SuperCommentContainer>
            ))}
        </Container>
    );
};
