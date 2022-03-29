export interface TaskCountType {
    taskStatusName: string;
    taskCount: number;
}

export interface ManagerTaskCountType {
    managerName: string;
    taskCounts: Array<TaskCountType>;
}
