import React from 'react';

import { useHistory } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import EmptySrc from '../../../public/assets/images/empty.png';
import { menuPath } from '../../pages/common';
import { projectState } from '../../stores/projectState';
import { ProjectType } from '../../types/project';
import {
    ProjectIcon,
    VerticalBar,
    ProjectContainer,
    MenuContainer,
    Project,
    Menu,
    ProjectWrapper,
    MenuWrapper,
} from './style';

interface Props {
    props: Array<ProjectType | string>;
    project?: boolean;
}

export const SideBar = ({ props, project }: Props) => {
    const history = useHistory();
    const [currentProjectState, setProjectState] = useRecoilState(projectState);

    const isProjectType = (el: ProjectType | string): el is ProjectType => (el as ProjectType).name !== undefined;

    const enterProject = (project: ProjectType) => {
        const { id, name, description, thumbnail } = project;
        setProjectState({
            id,
            name,
            description,
            thumbnail,
            members: [],
        });
        history.push({
            pathname: '/kanban',
            state: { projectId: id },
        });
    };

    const handleClickSideBar = (idx: number) => {
        history.push({
            pathname: menuPath[idx],
            state: { projectId: currentProjectState.id },
        });
    };

    return (
        <>
            <VerticalBar />
            {project ? (
                <ProjectContainer>
                    <ProjectWrapper>
                        {props.map((el, idx) =>
                            isProjectType(el) ? (
                                <Project key={idx} onClick={() => enterProject(el)}>
                                    <ProjectIcon src={el.thumbnail ? el.thumbnail : EmptySrc} />
                                    <span>{el.name}</span>
                                </Project>
                            ) : null,
                        )}
                    </ProjectWrapper>
                </ProjectContainer>
            ) : (
                <MenuContainer>
                    <MenuWrapper>
                        {props.map((el, idx) => (
                            <div key={idx} onClick={() => handleClickSideBar(idx)}>
                                <Menu>{el}</Menu>
                            </div>
                        ))}
                    </MenuWrapper>
                </MenuContainer>
            )}
        </>
    );
};
