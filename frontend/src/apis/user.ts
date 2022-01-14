import { client } from './common';

interface userProfile {
    displayName: string;
    githubId: string;
    avatar: string;
}

export const getUserProfile = async () => {
    const response = await client.get<userProfile>(`/users/profile`);

    return response;
};

export const updateDisplayName = async (displayName: string) => {
    const response = await client.patch(`/users/name`, { name: displayName });

    return response;
};

export const createProject = async (data: FormData) => {
    const response = await client.post(`/users/projects`, data, {
        headers: { 'Content-Type': 'multipart/form-data' },
    });

    return response;
};
