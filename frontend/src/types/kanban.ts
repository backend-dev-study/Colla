export interface TaskType {
    id: number;
    title: string;
    managerName: string;
    priority: number;
    column: string;
    index?: number;
}

export interface ItemType {
    id: number;
    index: number;
    name: string;
    type: string;
}
