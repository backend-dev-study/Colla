import React from 'react';

import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import EmptySrc from '../../../public/assets/images/empty.png';
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

function isProjectType(el: ProjectType | string): el is ProjectType {
    return (el as ProjectType).name !== undefined;
}

export const SideBar = ({ props, project }: Props) => {
    const history = useHistory();
    const setProjectState = useSetRecoilState(projectState);

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
                            <div key={idx}>
                                <Menu>{el}</Menu>
                            </div>
                        ))}
                    </MenuWrapper>
                </MenuContainer>
            )}
        </>
    );
};
