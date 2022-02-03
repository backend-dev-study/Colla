import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import noticeIconImg from '../../../../public/assets/images/notification_outline.svg';
import { decideInvitation } from '../../../apis/project';
import { getUserNotices } from '../../../apis/user';
import { NoticeType } from '../../../types/user';
import { Icon, Invitation, InvitationDecision, Notice, NoticeMessage, Wrapper } from './style';

const NoticeModal = () => {
    const [notices, setNotices] = useState<Array<NoticeType>>([]);

    const updateNotices = async () => {
        const res = await getUserNotices();
        setNotices(res.data);
    };

    const handleClick = async (notice: NoticeType, accept: boolean) => {
        await decideInvitation(notice.projectId!, notice.id, accept);
        await updateNotices();
    };

    const translateNotice = (notice: NoticeType) => {
        let message;
        switch (notice.noticeType) {
            case 'INVITE_USER':
                message = `${notice.projectName} 프로젝트로부터 초대 받았습니다.`;
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
                                <InvitationDecision onClick={() => handleClick(notice, true)}>수락</InvitationDecision>
                                <InvitationDecision onClick={() => handleClick(notice, false)}>거절</InvitationDecision>
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
        updateNotices();
    }, []);

    return <Wrapper>{notices.map((notice) => translateNotice(notice))}</Wrapper>;
};

export default NoticeModal;
