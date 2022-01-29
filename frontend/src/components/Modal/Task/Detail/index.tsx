import React, { FC, useState } from 'react';

import DownIconSrc from '../../../../../public/assets/images/down.png';
import { MemberDropDown } from '../../../DropDown/Member';
import { Priority } from '../../../List/Priority';
import { TagList } from '../../../List/TagList';
import { TagModal } from '../../Tag';
import { DownIcon } from '../style';
import { DetailComponent, DetailContainer, MemberList, Status } from './style';

interface PropType {
    status: string;
}

const dummy: Array<string> = [
    '백엔드',
    '프론트엔드',
    '리팩토링',
    '배포',
    'bug fix',
    'hot fix',
    'enhancement',
    'refactoring',
    'document',
];

export const DetailInfoContainer: FC<PropType> = ({ status }) => {
    const [manager, setManager] = useState('');
    const [memberVisible, setMemberVisible] = useState(false);
    const [tags, setTags] = useState(dummy);
    const [tagModalVisible, setTagModalVisible] = useState(false);

    const showMemberList = () => {
        setMemberVisible((prev) => !prev);
    };

    const showTagModal = () => {
        setTagModalVisible((prev) => !prev);
    };

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
                    <Priority />
                </DetailComponent>
                <DetailComponent>
                    태그
                    <TagList tags={tags} showTagModal={showTagModal} />
                </DetailComponent>
            </DetailContainer>
            {memberVisible ? <MemberDropDown setManager={setManager} setMemberVisible={setMemberVisible} /> : null}
            {tagModalVisible ? <TagModal showTagModal={showTagModal} setTags={setTags} /> : null}
        </>
    );
};
