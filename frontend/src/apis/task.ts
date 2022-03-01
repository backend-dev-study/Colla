import { StoryTaskType, TaskResponseType } from '../types/task';
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

export const updateTask = async (taskId: number, data: FormData) => {
    const response = await client.put(`/projects/tasks/${taskId}`, data, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    return response;
};

export const updateTaskStatus = async (taskId: number, statusName: string) => {
    const response = await client.patch(`/projects/tasks/${taskId}`, { statusName });

    return response;
};

export const getTasksGroupByStory = async (projectId: number) => {
    const response = await client.get<Array<StoryTaskType>>(`/projects/${projectId}/tasks/story`);

    return response;
};
