import { ManagerTaskCountType, TaskCountType } from '../types/dashboard';
import { TaskType } from '../types/roadmap';
import { SimpleTaskType, StoryTaskType, TaskResponseType } from '../types/task';
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

export const updateTaskStatus = async (projectId: number, taskId: number, statusName: string) => {
    const response = await client.patch(`/projects/${projectId}/tasks/${taskId}`, { statusName });

    return response;
};

export const getStoryTasks = async (projectId: number, storyId: number) => {
    const response = await client.get<Array<TaskType>>(`/projects/${projectId}/stories/${storyId}/tasks`);

    return response;
};

export const getTasksGroupByStory = async (projectId: number) => {
    const response = await client.get<Array<StoryTaskType>>(`/projects/${projectId}/tasks/story`);

    return response;
};

export const getTaskCountByStatus = async (projectId: number) => {
    const response = await client.get<Array<TaskCountType>>(`/projects/${projectId}/tasks/count`);

    return response;
};

export const getTaskCountByManagerAndStatus = async (projectId: number) => {
    const response = await client.get<Array<ManagerTaskCountType>>(`/projects/${projectId}/tasks/count/manager`);

    return response;
};

export const getTasksFilterByStatus = async (projectId: number, statuses: string) => {
    const response = await client.get<Array<SimpleTaskType>>(
        `/projects/${projectId}/tasks/statuses?statuses=${statuses}`,
    );

    return response;
};

export const getTasksFilterByManager = async (projectId: number, managers: string, includeNoManager: boolean) => {
    const response = await client.get<Array<SimpleTaskType>>(
        `/projects/${projectId}/tasks/managers?managers=${managers}&notSelected=${includeNoManager}`,
    );

    return response;
};

export const getTasksFilterByTags = async (projectId: number, tags: string) => {
    const response = await client.get<Array<SimpleTaskType>>(`/projects/${projectId}/tasks/tags?tags=${tags}`);

    return response;
};

export const getTasksOrderByCreatedDate = async (projectId: number, ascending: boolean) => {
    const response = await client.get<Array<SimpleTaskType>>(
        `/projects/${projectId}/tasks/created-date?ascending=${ascending}`,
    );

    return response;
};

export const getTasksOrderByPriority = async (projectId: number, ascending: boolean) => {
    const response = await client.get<Array<SimpleTaskType>>(
        `/projects/${projectId}/tasks/priority?ascending=${ascending}`,
    );
    return response;
};

export const searchTasksByKeyword = async (projectId: number, keyword: string) => {
    const response = await client.get(`/projects/${projectId}/tasks/search?keyword=${keyword}`);

    return response;
};
