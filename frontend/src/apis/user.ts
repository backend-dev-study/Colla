import { ProjectType } from '../types/project';
import { NoticeType, UserProfile } from '../types/user';
import { client } from './common';

export const getUserProfile = async () => {
    const response = await client.get<UserProfile>(`/users/profile`);

    return response;
};

export const updateDisplayName = async (displayName: string) => {
    const response = await client.patch(`/users/name`, { name: displayName });

    return response;
};

export const createProject = async (data: FormData) => {
    const response = await client.post<ProjectType>(`/users/projects`, data, {
        headers: { 'Content-Type': 'multipart/form-data' },
    });

    return response;
};

export const getUserProjects = async () => {
    const response = await client.get(`/users/projects`);

    return response;
};

export const getUserNotices = async () => {
    const response = await client.get<Array<NoticeType>>('/users/notices');

    return response;
};
