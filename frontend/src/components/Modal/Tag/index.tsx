import React, { ChangeEvent, FC, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { createTag } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { ButtonContainer, CancelButton, CompleteButton } from '../Task/style';
import { Container, Tag, TagInput } from './style';

interface PropType {
    showTagModal: Function;
    setTags: Function;
}

export const TagModal: FC<PropType> = ({ showTagModal, setTags }) => {
    const [tag, setTag] = useState<string>('');
    const project = useRecoilValue(projectState);

    const handleInputTag = (event: ChangeEvent) => {
        setTag((event.target as HTMLInputElement).value);
    };

    const handleCompleteButton = async () => {
        try {
            const res = await createTag(project.id, tag);
            setTags((prev: Array<string>) => [...prev, res.data]);
            showTagModal();
        } catch (err) {
            showTagModal();
        }
    };

    return (
        <Container>
            <Tag>
                <span>태그 등록하기</span>
                <TagInput type="text" value={tag} onChange={handleInputTag} />
            </Tag>
            <ButtonContainer>
                <CancelButton onClick={() => showTagModal()}>취소</CancelButton>
                <CompleteButton onClick={handleCompleteButton}>완료</CompleteButton>
            </ButtonContainer>
        </Container>
    );
};
