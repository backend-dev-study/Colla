export interface SearchPlaceType {
    name: string;
    image?: string;
    latitude: number;
    longitude: number;
    address: string;
}

export interface MeetingPlaceType extends SearchPlaceType {
    id: number;
}

export interface LatLngType {
    lng: number | undefined;
    lat: number | undefined;
}
