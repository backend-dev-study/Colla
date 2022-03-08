import { client } from './common';

export const updateStoryPeriod = async (storyId: number, data: FormData) => {
    const response = await client.patch(`/projects/stories/${storyId}/period`, data, {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    });

    return response;
};
