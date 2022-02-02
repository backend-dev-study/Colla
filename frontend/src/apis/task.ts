import { client } from './common';

export const createTask = async (data: FormData) => {
    const response = await client.post(`/users/projects`, data, {
        headers: { 'Content-Type': 'multipart/form-data' },
    });

    return response;
};
