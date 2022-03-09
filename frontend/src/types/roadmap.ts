export interface StoryType {
    id: number;
    title: string;
    startAt?: string;
    endAt?: string;
}

export interface TaskType {
    title: string;
    manager: string;
    tags: Array<string>;
}
