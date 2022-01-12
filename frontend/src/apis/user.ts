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

export const createProject = async (userId: number, projectName: string, projectDesc: string) => {
    const response = await client.post(`/users/${userId}/projects`, { name: projectName, description: projectDesc });

    return response;
};
