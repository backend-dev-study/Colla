import styled from '@emotion/styled';

import { GREEN, LIGHT_GREEN } from '../../../styles/color';
import { Column, Modal, WidthAround } from '../../../styles/common';

export const Wrapper = styled.div`
    position: absolute;
    top: 40px;
    right: 0px;
    border-radius: 20px;
    padding: 30px 30px 30px 30px;
    width: 400px;
    background: ${GREEN};

    ${Modal}
`;

export const Notice = styled.div`
    height: 80px;
    padding: 10px;
    margin-top: 12px;
    margin-bottom: 12px;
    border-radius: 10px;
    font-size: 18px;
    background: ${(props: { check: boolean }) => (props.check ? LIGHT_GREEN : 'white')};
    pointer-events: ${(props: { check: boolean }) => (props.check ? 'none' : 'auto')};

    ${WidthAround}
`;

export const Icon = styled.img`
    flex: 1;
    margin-left: 10px;
    margin-right: 10px;
    width: 40px;
`;

export const NoticeMessage = styled.div`
    flex: 8;
    justify-content: space-evenly;
    align-items: center;

    ${Column}
`;

export const Invitation = styled.div`
    width: 160px;
    display: flex;
    justify-content: space-around;
    font-size: 15px;
`;

export const InvitationDecision = styled.button`
    font-size: 17px;
    border-radius: 5px;
`;
