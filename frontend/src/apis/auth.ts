import axios from 'axios';

export const getAccessToken = async (code: string) => {
    const response = await axios.get<{ accessToken: string }>(`${process.env.API_URL}/auth/login?code=${code}`);

    return response.data;
};
