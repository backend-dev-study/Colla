import { ProjectType } from '../types/project';
import { client } from './common';

interface task {
    id: number;
    title: string;
    managerName: string;
    avatar: string;
    priority: number;
}

interface ProjectAllType extends ProjectType {
    managerId: number;
    tasks: {
        [key: string]: Array<task>;
    };
}

interface projectTags {
    name: string;
}

export const getProject = async (projectId: number) => {
    const response = await client.get<ProjectAllType>(`/projects/${projectId}`);

    return response;
};

export const createStory = async (projectId: number, title: string) => {
    const response = await client.post(`/projects/${projectId}/stories`, { title });

    return response;
};

export const getProjectStories = async (projectId: number) => {
    const response = await client.get(`/projects/${projectId}/stories`);

    return response;
};

export const getProjectMembers = async (projectId: number) => {
    const response = await client.get(`/projects/${projectId}/members`);

    return response;
};

export const createTag = async (projectId: number, name: string) => {
    const response = await client.post(`/projects/${projectId}/tags`, { name });

    return response;
};

export const inviteUser = async (projectId: number, githubId: string) => {
    const response = await client.post(`/projects/${projectId}/members`, { githubId });

    return response;
};

export const getProjectTags = async (projectId: number) => {
    const response = await client.get<Array<projectTags>>(`/projects/${projectId}/tags`);

    return response;
};

export const decideInvitation = async (projectId: number, noticeId: number, accept: boolean) => {
    const response = await client.post(`/projects/${projectId}/members/decision`, { accept, noticeId });

    return response;
};
