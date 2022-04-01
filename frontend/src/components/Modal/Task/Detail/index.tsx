import React, { FC, useEffect, useState } from 'react';

import DownIconSrc from '../../../../../public/assets/images/down.png';
import { DetailInputType } from '../../../../types/task';
import { MemberDropDown } from '../../../DropDown/Member';
import { Priority } from '../../../List/Priority';
import { TagList } from '../../../List/Tag';
import { DownIcon } from '../style';
import { DetailComponent, DetailContainer, MemberList, Status } from './style';

interface PropType {
    status: string;
    detailInfoInput: DetailInputType;
}

export const DetailInfoContainer: FC<PropType> = ({ status, detailInfoInput }) => {
    const { taskInput, handleChangeManagerId, handleChangeStatus, handleChangePriority, handleSelectTag } =
        detailInfoInput;
    const { managerId, priority, selectedTags } = taskInput;
    const [manager, setManager] = useState('');
    const [memberVisible, setMemberVisible] = useState(false);

    const showMemberList = () => {
        setMemberVisible((prev) => !prev);
    };

    useEffect(() => {
        handleChangeStatus(status);
    }, []);

    useEffect(() => {
        if (!parseInt(managerId, 10)) {
            setManager(managerId);
        }
    }, [managerId]);

    return (
        <>
            <DetailContainer>
                <DetailComponent>
                    담당자
                    <MemberList onClick={showMemberList}>
                        {manager}
                        <DownIcon src={DownIconSrc} />
                    </MemberList>
                </DetailComponent>
                <DetailComponent>
                    상태
                    <Status>{status}</Status>
                </DetailComponent>
                <DetailComponent>
                    우선순위
                    <Priority priority={priority} handleChangePriority={handleChangePriority} />
                </DetailComponent>
                <DetailComponent>
                    태그
                    <TagList selectedTags={selectedTags} handleSelectTag={handleSelectTag} />
                </DetailComponent>
            </DetailContainer>
            {memberVisible ? (
                <MemberDropDown
                    setManager={setManager}
                    setMemberVisible={setMemberVisible}
                    handleChangeManagerId={handleChangeManagerId}
                />
            ) : null}
        </>
    );
};
