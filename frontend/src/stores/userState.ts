import { selector, useRecoilValueLoadable } from 'recoil';
import { getUserProfile } from '../apis/user';

export const userState = selector({
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

const useUserState = () => useRecoilValueLoadable(userState);

export default useUserState;
