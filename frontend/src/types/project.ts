import { TaskType } from './kanban';
import { UserProfile } from './user';

export interface ProjectType {
    id: number;
    name: string;
    description: string;
    thumbnail: string;
    members: Array<UserProfile>;
}

export interface ProjectAllType extends ProjectType {
    managerId: number;
    tasks: {
        [key: string]: Array<TaskType>;
    };
}

export interface ProjectTagType {
    name: string;
}

export interface ProjectMemberType {
    id: number;
    name: string;
    avatar: string;
}
