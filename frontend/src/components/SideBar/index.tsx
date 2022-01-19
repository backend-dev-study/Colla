import React from 'react';

import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import EmptySrc from '../../../public/assets/images/empty.png';
import { projectState } from '../../stores/projectState';
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

interface Project {
    id: number;
    name: string;
    description: string;
    thumbnail: string;
}

interface Props {
    props: Array<Project | string>;
    project?: boolean;
}

export const SideBar = ({ props, project }: Props) => {
    const history = useHistory();
    const setProjectState = useSetRecoilState(projectState);

    // eslint-disable-next-line no-shadow
    const enterProject = (project: Project) => {
        const { id, name, description, thumbnail } = project;
        setProjectState({
            id,
            name,
            description,
            thumbnail,
        });
        history.push('/kanban');
    };

    return (
        <>
            <VerticalBar />
            {project ? (
                <ProjectContainer>
                    <ProjectWrapper>
                        {props.map((el, idx) => (
                            <Project key={idx} onClick={() => enterProject(el as Project)}>
                                <ProjectIcon src={(el as Project).thumbnail ? (el as Project).thumbnail : EmptySrc} />
                                <span>{(el as Project).name}</span>
                            </Project>
                        ))}
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
