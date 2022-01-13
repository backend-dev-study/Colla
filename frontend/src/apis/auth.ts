import { client } from './common';

export const getAccessToken = async (code: string) => {
    const response = await client.get(`/auth/login?code=${code}`);

    return response;
};

export const logout = async () => {
    const response = await client.post(`/auth/logout`);

    return response;
};
