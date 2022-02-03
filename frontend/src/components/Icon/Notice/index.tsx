import React from 'react';

import noticeImg from '../../../../public/assets/images/notification_outline.svg';
import useModal from '../../../hooks/useModal';
import NoticeModal from '../../Modal/Notice';
import { ImageContainer, NoticeButton } from './style';

const NoticeIcon = () => {
    const { Modal, setModal } = useModal();
    return (
        <>
            <NoticeButton onClick={setModal}>
                <ImageContainer src={noticeImg} />
            </NoticeButton>
            <Modal>
                <NoticeModal />
            </Modal>
        </>
    );
};

export default NoticeIcon;
