import { MeetingPlaceType, SearchPlaceType } from '../types/meeting-place';
import { client } from './common';

export const getSpecificAreaMeetingPlace = async (
    projectId: number,
    minLng: number,
    maxLng: number,
    minLat: number,
    maxLat: number,
) => {
    const response = await client.get<Array<MeetingPlaceType>>(
        `/projects/${projectId}/meeting-places/boundary?minLng=${minLng}&maxLng=${maxLng}&minLat=${minLat}&maxLat=${maxLat}`,
    );

    return response;
};

export const createMeetingPlace = async (projectId: number, place: SearchPlaceType) => {
    const response = await client.post<MeetingPlaceType>(`/projects/${projectId}/meeting-places`, place);

    return response;
};

export const getMeetingPlaces = async (projectId: number) => {
    const response = await client.get<Array<MeetingPlaceType>>(`/projects/${projectId}/meeting-places`);

    return response;
};

export const deleteMeetingPlace = async (meetingPlaceId: number) => {
    const response = await client.delete(`/projects/meeting-places/${meetingPlaceId}`);

    return response;
};
