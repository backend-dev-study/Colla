import { atom, selector } from 'recoil';

export const projectNameState = atom({
    key: 'projectName',
    default: null,
});

export const projectDescState = atom({
    key: 'projectDesc',
    default: null,
});

export const projectInfoState = selector({
    key: 'project',
    get: ({ get }) => ({
        name: get(projectNameState),
        desc: get(projectDescState),
    }),
});
