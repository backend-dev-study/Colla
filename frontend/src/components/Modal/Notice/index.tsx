import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import noticeIconImg from '../../../../public/assets/images/notification_outline.svg';
import { decideInvitation } from '../../../apis/project';
import { getUserNotices } from '../../../apis/user';
import { NoticeType } from '../../../types/user';
import { Icon, Invitation, InvitationDecision, Notice, NoticeMessage, Wrapper } from './style';

const dummyNotices = [
    {
        id: 12313,
        noticeType: 'INVITE_USER',
        isChecked: true,
    },
    {
        id: 15313,
        noticeType: 'MENTION_USER',
        isChecked: false,
        mentionedURL: '/home',
    },
];
const NoticeModal = () => {
    const [notices, setNotices] = useState<Array<NoticeType>>(dummyNotices);

    const handleNotices = async () => {
        const res = await getUserNotices();
        setNotices(res.data);
    };

    const translateNotice = (notice: NoticeType) => {
        let message;
        switch (notice.noticeType) {
            case 'INVITE_USER':
                message = `${notice.projectId} 프로젝트로부터 초대 받았습니다.`;
                break;
            case 'MENTION_USER':
                message = `${notice.mentionedURL}에서 언급되었습니다.`;
                break;
            default:
                message = '잘못된 알림입니다.';
        }

        return (
            <Notice key={notice.id} check={notice.isChecked}>
                <Icon src={noticeIconImg} />
                <NoticeMessage>
                    {notice.noticeType === 'INVITE_USER' ? (
                        <>
                            <div>{message}</div>
                            <Invitation>
                                <InvitationDecision onClick={() => decideInvitation(notice.projectId!, true)}>
                                    수락
                                </InvitationDecision>
                                <InvitationDecision onClick={() => decideInvitation(notice.projectId!, false)}>
                                    거절
                                </InvitationDecision>
                            </Invitation>
                        </>
                    ) : (
                        <Link to={notice.mentionedURL!}>
                            <div>{message}</div>
                        </Link>
                    )}
                </NoticeMessage>
            </Notice>
        );
    };

    useEffect(() => {
        handleNotices();
    }, []);

    return <Wrapper>{notices.map((notice) => translateNotice(notice))}</Wrapper>;
};

export default NoticeModal;
