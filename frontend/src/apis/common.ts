import axios from 'axios';
import { toast } from 'react-toastify';

export const client = axios.create({
    baseURL: process.env.API_URL,
    withCredentials: true,
});

interface Error {
    response: {
        data: {
            status: number;
            message: string;
        };
    };
}

const errorHandler = (error: Error) => {
    toast.error(error.response.data.message);
    return Promise.reject(error);
};

client.interceptors.response.use(
    (response) => response,
    (error) => errorHandler(error),
);
