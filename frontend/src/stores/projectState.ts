import { atom, selector } from 'recoil';
import { ProjectType } from '../types/project';

export const projectState = atom<ProjectType>({
    key: 'project',
    default: {
        id: -1,
        name: '',
        description: '',
        thumbnail: '',
        members: [],
    },
});

export const projectMemberState = selector({
    key: 'projectMember',
    get: ({ get }) => {
        const project = get(projectState);
        return project.members;
    },
});
