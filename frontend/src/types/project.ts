import { UserProfile } from './user';

export interface ProjectType {
    id: number;
    name: string;
    description: string;
    thumbnail: string;
    members: Array<UserProfile>;
}

export interface ProjectTagType {
    name: string;
}

export interface ProjectMemberType {
    id: number;
    name: string;
    avatar: string;
}
