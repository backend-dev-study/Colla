import { selector, useRecoilValueLoadable } from 'recoil';
import { isResponseSuccess } from '../apis/common';
import { getUserProfile } from '../apis/user';

const userState = selector({
    key: 'userState',
    get: async () => {
        const res = await getUserProfile();
        if (!isResponseSuccess(res.status)) {
            return null;
        }
        return res.data;
    },
});

const useUserState = () => useRecoilValueLoadable(userState);

export default useUserState;
