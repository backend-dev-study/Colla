import React, { FC, useEffect, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { getProjectMembers } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { ProjectMemberType } from '../../../types/project';
import { Avatar, Name } from '../../Task/style';
import { Member, MemberList } from './style';

interface PropType {
    setManager: Function;
    setMemberVisible: Function;
    handleChangeManagerId: Function;
}

export const MemberDropDown: FC<PropType> = ({ setManager, setMemberVisible, handleChangeManagerId }) => {
    const project = useRecoilValue(projectState);
    const [memberList, setMemberList] = useState<ProjectMemberType[]>([]);

    const selectManager = (id: number, name: string) => {
        setManager(name);
        handleChangeManagerId(id);
        setMemberVisible();
    };

    useEffect(() => {
        (async () => {
            const res = await getProjectMembers(project.id);
            setMemberList(res.data);
        })();
    }, []);

    return (
        <MemberList>
            {memberList.map(({ id, name, avatar }, idx) => (
                <Member key={idx} onClick={() => selectManager(id, name)}>
                    <Avatar src={avatar}></Avatar>
                    <Name>{name}</Name>
                </Member>
            ))}
        </MemberList>
    );
};
