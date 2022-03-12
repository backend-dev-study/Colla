import EmptyImg from '../../../../public/assets/images/empty.png';
import { MeetingPlaceType } from '../../../types/meeting-place';
import './style.scss';

export const InfoWindow = (meetingPlace: MeetingPlaceType) => `<div class='Container'>
                <img class='meeting-place-img' src=${EmptyImg}>
                <div class='meeting-place-info'>${meetingPlace.name}</div>
                <div class='meeting-place-info'>${meetingPlace.address}</div>
            </div>`;
