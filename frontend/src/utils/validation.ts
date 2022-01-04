import Cookies from 'js-cookie';

export const validateUser = () => !!Cookies.get('accessToken');
