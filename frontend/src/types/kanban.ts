export interface TaskType {
    id: number;
    name: string;
    column: string;
    index?: number;
}

export interface ItemType {
    id: number;
    index: number;
    name: string;
    type: string;
}
