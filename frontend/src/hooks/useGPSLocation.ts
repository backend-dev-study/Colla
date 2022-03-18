import { toast } from 'react-toastify';

const GPS_NOT_SUPPORT = '현재 기기가 위치 정보 추적을 지원하지 않습니다.';
const GPS_ERROR = '위치 정보 불러오기에 실패했습니다.';
const GPS_SUCCESS = '위치 정보 불러오기에 성공했습니다.';

const useGPSLocation = () => {
    if (navigator.geolocation) {
        return new Promise((resolve) => {
            navigator.geolocation.getCurrentPosition(
                (position: GeolocationPosition) => {
                    toast.success(GPS_SUCCESS);
                    resolve({ lng: position.coords.longitude, lat: position.coords.latitude });
                },
                () => {
                    toast.error(GPS_ERROR);
                    resolve({ lng: undefined, lat: undefined });
                },
            );
        });
    }
    toast.error(GPS_NOT_SUPPORT);
};

export default useGPSLocation;
