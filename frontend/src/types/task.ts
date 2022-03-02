export interface TaskInputType {
    title: string;
    description: string;
    managerId: string;
    priority: number;
    status: string;
    selectedTags: Array<string>;
    story: string;
    preTasks: Array<number>;
}

export interface BasicInputType {
    taskInput: TaskInputType;
    handleChangeTitle: Function;
    handleChangeDescription: Function;
    handleChangeStory: Function;
    handleAddPreTask: Function;
    handleDeletePreTask: Function;
}

export interface DetailInputType {
    taskInput: TaskInputType;
    handleChangeManagerId: Function;
    handleChangeStatus: Function;
    handleChangePriority: Function;
    handleSelectTag: Function;
}

export interface TaskResponseType {
    title: string;
    description: string;
    manager: string;
    priority: number;
    status: string;
    tags: Array<string>;
    story: string;
    preTasks: string;
}

export interface SimpleTaskType {
    id: number;
    title: string;
    priority: number;
    managerAvatar: string;
    tags: Array<string>;
}

export interface StoryTaskType {
    story: string;
    taskList: Array<SimpleTaskType>;
}
