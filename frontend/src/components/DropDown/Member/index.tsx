import React, { FC, useEffect, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { getProjectMembers } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { Avatar, Name } from '../../Task/style';
import { Member, MemberList } from './style';

interface PropType {
    setManager: Function;
    setMemberVisible: Function;
    handleChangeManagerId: Function;
}

interface MemberType {
    id: number;
    name: string;
    avatar: string;
}

export const MemberDropDown: FC<PropType> = ({ setManager, setMemberVisible, handleChangeManagerId }) => {
    const project = useRecoilValue(projectState);
    const [memberList, setMemberList] = useState<MemberType[]>([]);

    const selectManager = (id: number, name: string) => {
        setManager(name);
        handleChangeManagerId(id);
        setMemberVisible();
    };

    useEffect(() => {
        (async () => {
            try {
                const res = await getProjectMembers(project.id);
                setMemberList(res.data);
            } catch (err) {
                setMemberList([]);
            }
        })();
    }, []);

    return (
        <MemberList>
            {memberList.map((member, idx) => {
                const { id, name, avatar } = member;
                return (
                    <Member key={idx} onClick={() => selectManager(id, name)}>
                        <Avatar src={avatar}></Avatar>
                        <Name>{name}</Name>
                    </Member>
                );
            })}
        </MemberList>
    );
};
