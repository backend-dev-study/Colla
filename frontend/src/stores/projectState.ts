import { atom } from 'recoil';
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
