import { CommentType, CreateCommentType, UpdateCommentType } from '../types/comment';
import { client } from './common';

export const saveComment = async (taskId: number, contents: string, superCommentId: number | null) => {
    const response = await client.post<CreateCommentType>(`/tasks/${taskId}/comments`, { contents, superCommentId });

    return response;
};

export const getAllComments = async (taskId: number) => {
    const response = await client.get<Array<CommentType>>(`tasks/${taskId}/comments`);

    return response;
};

export const modifyComment = async (commentId: number, contents: string) => {
    const response = await client.put<UpdateCommentType>(`tasks/comments/${commentId}`, { contents });

    return response;
};

export const deleteComment = async (commentId: number) => {
    const response = await client.delete(`tasks/comments/${commentId}`);

    return response;
};
