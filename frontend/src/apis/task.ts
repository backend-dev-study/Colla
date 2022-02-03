import { client } from './common';

export const createTask = async (data: FormData) => {
    const response = await client.post(`/tasks/`, data, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    return response;
};