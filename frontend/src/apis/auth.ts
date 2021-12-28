import axios from 'axios';

export const getAccessToken = async (code: string) => {
    const response = await axios.get<{ accessToken: string }>(`${process.env.API_URL}?code=${code}`);

    return response.data;
};
