import axios from 'axios';

export const client = axios.create({
    baseURL: process.env.API_URL,
    withCredentials: true,
});

export const isResponseSuccess = (status: number) => {
    if (status >= 200 && status < 300) return true;
    return false;
};
