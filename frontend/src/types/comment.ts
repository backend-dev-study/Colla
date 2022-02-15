import { UserProfile } from './user';

export interface CreateCommentType {
    id: number;
    writer: UserProfile;
    superCommentId: number;
    contents: string;
}

export interface CommentType {
    id: number;
    writer: UserProfile;
    contents: string;
    subComments: Array<CommentType>;
}
