export interface SearchPlaceType {
    name: string;
    latitude: number;
    longitude: number;
    category: string;
    address: string;
}

export interface MeetingPlaceType extends SearchPlaceType {
    id: number;
}

export interface LatLngType {
    lng: number | undefined;
    lat: number | undefined;
}
