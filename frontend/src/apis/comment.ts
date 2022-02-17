import { CommentType, CreateCommentType } from '../types/comment';
import { client } from './common';

export const saveComment = async (taskId: number, contents: string, superCommentId: number | null) => {
    const response = await client.post<CreateCommentType>(`/tasks/${taskId}/comments`, { contents, superCommentId });

    return response;
};

export const getAllComments = async (taskId: number) => {
    const response = await client.get<Array<CommentType>>(`tasks/${taskId}/comments`);

    return response;
};

export const modifyComment = async (taskId: number, commentId: number, contents: string) => {
    const response = await client.put(`tasks/${taskId}/comments/${commentId}`, { contents });

    return response;
};
