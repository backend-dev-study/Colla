import { atom } from 'recoil';

interface project {
    id: number;
    name: string;
    description: string;
    thumbnail: string;
}

export const projectState = atom<project>({
    key: 'project',
    default: {
        id: -1,
        name: '',
        description: '',
        thumbnail: '',
    },
});
