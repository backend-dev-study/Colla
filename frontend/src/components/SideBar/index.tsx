import React from 'react';

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
    name: string;
}

interface Props {
    props: Array<Project | string>;
    project?: boolean;
}

export const SideBar = ({ props, project }: Props) => (
    <>
        <VerticalBar />
        {project ? (
            <ProjectContainer>
                <ProjectWrapper>
                    {props.map((el, idx) => (
                        <Project key={idx}>
                            <ProjectIcon />
                            <span>{(el as Project).name}</span>
                        </Project>
                    ))}
                </ProjectWrapper>
            </ProjectContainer>
        ) : (
            <MenuContainer>
                <MenuWrapper>
                    {props.map((el) => (
                        <>
                            <Menu>{el}</Menu>
                        </>
                    ))}
                </MenuWrapper>
            </MenuContainer>
        )}
    </>
);
