import { selector, useRecoilValueLoadable } from 'recoil';
import { getUserProfile } from '../apis/user';

const userState = selector({
    key: 'userState',
    get: async () => {
        try {
            const res = await getUserProfile();
            return res.data;
        } catch (e) {
            return null;
        }
    },
});

export const useUserState = () => useRecoilValueLoadable(userState);
