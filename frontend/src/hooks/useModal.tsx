import React, { useEffect, useState } from 'react';
import styled from '@emotion/styled';
import { Center } from '../styles/common';

const Absolute = styled.div`
    position: absolute;

    ${Center}
`;

const useModal = () => {
    const [visible, setVisible] = useState(false);

    const onClickOutside = () => setVisible(false);

    useEffect(() => {
        window.addEventListener('click', onClickOutside);
        return () => window.removeEventListener('click', onClickOutside);
    }, []);

    const Modal = ({ children }: any) => (
        <>{visible ? <Absolute onClick={(e) => e.stopPropagation()}>{children}</Absolute> : null}</>
    );

    return {
        setModal: (e: any) => {
            const prev = visible;
            window.dispatchEvent(new Event('click'));
            setVisible(!prev);
            e.stopPropagation();
        },
        Modal,
    };
};

export default useModal;
