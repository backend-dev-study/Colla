import { client } from './common';

export const createProject = async (userId: number, projectName: string, projectDesc: string) => {
    const response = await client.post(`/users/${userId}/projects`, { name: projectName, description: projectDesc });
    return response;
};

export const updateDisplayName = async (displayName: string) => {
    const response = await client.patch(`/users/name`, { name: displayName });

    return response;
};
