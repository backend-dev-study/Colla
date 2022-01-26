import { client } from './common';

interface member {
    displayName: string;
    githubId: string;
    avatar: string;
}

interface task {
    id: number;
    title: string;
    managerName: string;
    avatar: string;
    priority: number;
}

interface project {
    id: number;
    managerId: number;
    name: string;
    description: string;
    thumbnail: string;
    members: Array<member>;
    tasks: {
        [key: string]: Array<task>;
    };
}

export const getProject = async (projectId: number) => {
    const response = await client.get<project>(`/projects/${projectId}`);

    return response;
};
