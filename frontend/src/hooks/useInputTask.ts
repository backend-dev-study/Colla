import React, { useState } from 'react';

import { useRecoilValue } from 'recoil';
import { createTask, updateTask } from '../apis/task';
import { projectState } from '../stores/projectState';
import { TaskInputType, TaskResponseType } from '../types/task';

const useInputTask = () => {
    const project = useRecoilValue(projectState);
    const [taskInput, setTaskInput] = useState<TaskInputType>({
        title: '',
        description: '',
        managerId: '',
        priority: 5,
        status: '',
        selectedTags: [],
        story: '',
        preTasks: [],
    });
    const { title, description, managerId, priority, status, selectedTags, story, preTasks } = taskInput;

    const handleCompleteButton = async (taskId: number | null, page: string) => {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('description', description);
        formData.append('managerId', managerId);
        formData.append('priority', JSON.stringify(priority));
        formData.append('status', status);
        formData.append('tags', JSON.stringify(selectedTags));
        formData.append('projectId', JSON.stringify(project.id));
        formData.append('story', story);
        formData.append('preTasks', JSON.stringify(preTasks));

        taskId ? await updateTask(taskId!, formData) : await createTask(formData);
        window.location.replace(`/${page}`);
    };

    const handleChangeTitle = (e: React.ChangeEvent) => {
        setTaskInput({ ...taskInput, title: (e.target as HTMLInputElement).value });
    };

    const handleChangeDescription = (e: React.ChangeEvent) => {
        setTaskInput({ ...taskInput, description: (e.target as HTMLInputElement).value });
    };

    const handleChangeStory = (newStory: string) => {
        setTaskInput({ ...taskInput, story: newStory });
    };

    const handleAddPreTask = (taskId: number) => {
        setTaskInput({ ...taskInput, preTasks: [...preTasks, taskId] });
    };

    const handleDeletePreTask = (taskId: number) => {
        setTaskInput({ ...taskInput, preTasks: preTasks.filter((el) => el !== taskId) });
    };

    const handleChangeManagerId = (id: string) => {
        setTaskInput({ ...taskInput, managerId: id });
    };

    const handleChangeStatus = (newStatus: string) => {
        setTaskInput({ ...taskInput, status: newStatus });
    };

    const handleChangePriority = (newPriority: number) => {
        setTaskInput({ ...taskInput, priority: newPriority });
    };

    const handleSelectTag = (newSelectedTags: Array<string>) => {
        setTaskInput({ ...taskInput, selectedTags: newSelectedTags });
    };

    const setSelectedTask = (task: TaskResponseType) => {
        const { title, description, story, preTasks, manager, status, priority, tags } = task;
        setTaskInput({
            title,
            description,
            story: story || '',
            preTasks: JSON.parse(preTasks),
            managerId: manager || '',
            status,
            priority,
            selectedTags: tags,
        });
    };

    const clearInputTask = () => {
        setTaskInput({
            title: '',
            description: '',
            managerId: '',
            priority: 5,
            status: '',
            selectedTags: [],
            story: '',
            preTasks: [],
        });
    };

    return {
        basicInfoInput: {
            taskInput,
            handleChangeTitle,
            handleChangeDescription,
            handleChangeStory,
            handleAddPreTask,
            handleDeletePreTask,
        },
        detailInfoInput: {
            taskInput,
            handleChangeManagerId,
            handleChangeStatus,
            handleChangePriority,
            handleSelectTag,
        },
        handleCompleteButton,
        setSelectedTask,
        clearInputTask,
    };
};

export default useInputTask;
