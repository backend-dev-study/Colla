import { StoryType } from '../types/roadmap';
import { client } from './common';

export const getProjectStories = async (projectId: number) => {
    const response = await client.get<Array<StoryType>>(`/projects/${projectId}/all-stories`);

    return response;
};

export const updateStoryPeriod = async (storyId: number, data: FormData) => {
    const response = await client.patch(`/projects/stories/${storyId}/period`, data, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    return response;
};
