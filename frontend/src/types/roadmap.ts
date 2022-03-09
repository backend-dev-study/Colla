export interface StoryType {
    id: number;
    title: string;
    startAt: string | null;
    endAt: string | null;
}

export interface TaskType {
    title: string;
    manager: string;
    tags: Array<string>;
}
