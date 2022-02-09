import { TaskResponseType } from '../types/task';
import { client } from './common';

export const createTask = async (data: FormData) => {
    const response = await client.post(`/projects/tasks`, data, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    return response;
};

export const getTask = async (taskId: number) => {
    const response = await client.get<TaskResponseType>(`/projects/tasks/${taskId}`);

    return response;
};
