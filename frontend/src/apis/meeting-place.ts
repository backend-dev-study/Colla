import { MeetingPlaceType, SearchPlaceType } from '../types/meeting-place';
import { client } from './common';

export const createMeetingPlace = async (projectId: number, place: SearchPlaceType) => {
    const response = await client.post<MeetingPlaceType>(`/projects/${projectId}/meeting-places`, place);

    return response;
};
