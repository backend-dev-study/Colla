import client from './client';

export const createProject = async (userId: number, projectName: string, projectDesc: string) => {
    const response = await client.post(`/users/${userId}/projects`, { name: projectName, description: projectDesc });
    return response.data;
};
