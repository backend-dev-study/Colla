import { MeetingPlaceType } from '../types/meeting-place';
import { client } from './common';

export const getSpecificAreaMeetingPlace = async (
    projectId: number,
    minLng: number,
    maxLng: number,
    minLat: number,
    maxLat: number,
) => {
    const response = await client.get<Array<MeetingPlaceType>>(
        `/projects/${projectId}/meeting-places?minLng=${minLng}&maxLng=${maxLng}&minLat=${minLat}&maxLat=${maxLat}`,
    );

    return response;
};
