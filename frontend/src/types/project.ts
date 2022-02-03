import { UserProfile } from './user';

export interface ProjectType {
    id: number;
    name: string;
    description: string;
    thumbnail: string;
    members: Array<UserProfile>;
}
