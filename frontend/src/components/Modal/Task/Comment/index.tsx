import React, { ChangeEvent, FC, useEffect, useState } from 'react';

import { getAllComments, saveComment } from '../../../../apis/comment';
import { CommentType } from '../../../../types/comment';
import { CommentList } from '../../../List/Comment';
import { Comment, CommentInput, Container, SaveButton, Title } from './style';

interface PropType {
    taskId: number;
}

export const CommentContainer: FC<PropType> = ({ taskId }) => {
    const [comment, setComment] = useState<string>('');
    const [commentList, setCommentList] = useState<Array<CommentType>>([]);

    const handleChangeComment = (event: ChangeEvent) => {
        setComment((event.target as HTMLInputElement).value);
    };

    const handleSaveButton = async () => {
        const res = await saveComment(taskId, comment, null);
        setCommentList((prev) => {
            const { id, writer, superCommentId, contents } = res.data;
            const currentList = [...prev];

            superCommentId
                ? currentList
                      .filter((comment) => comment.id === superCommentId)
                      .forEach((comment) => comment.subComments.push({ id, writer, contents, subComments: [] }))
                : currentList.push({ id, writer, contents, subComments: [] });

            return currentList;
        });
        setComment('');
    };

    useEffect(() => {
        (async () => {
            const res = await getAllComments(taskId);
            setCommentList(res.data);
        })();
    }, []);

    return (
        <Container>
            <Title>댓글</Title>
            <Comment>
                <CommentInput value={comment} onChange={handleChangeComment} />
                <SaveButton onClick={handleSaveButton}>등록</SaveButton>
            </Comment>
            <CommentList commentList={commentList} />
        </Container>
    );
};
