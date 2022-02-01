import React, { FC, useEffect, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { getProjectTags } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { TagModal } from '../../Modal/Tag';
import { Container, Grid, Tag } from './style';

export const TagList: FC = () => {
    const project = useRecoilValue(projectState);
    const [tags, setTags] = useState<Array<string>>([]);
    const [tagModalVisible, setTagModalVisible] = useState(false);
    const [selectedTags, setSelectedTags] = useState<Array<number>>([]);

    const showTagModal = () => {
        setTagModalVisible((prev) => !prev);
    };

    const selectTag = (idx: number) => {
        selectedTags.includes(idx)
            ? setSelectedTags((prev) => prev.filter((el) => el !== idx))
            : setSelectedTags((prev) => [...prev, idx]);
    };

    useEffect(() => {
        (async () => {
            try {
                const res = await getProjectTags(project.id);
                setTags(() => res.data.map((tag) => tag.name));
            } catch (err) {
                setTags([]);
            }
        })();
    });

    return (
        <Container>
            <Grid>
                {tags.map((data, idx) => (
                    <Tag
                        key={idx}
                        className={selectedTags.includes(idx) ? 'selected' : ''}
                        onClick={() => selectTag(idx)}
                    >
                        {data}
                    </Tag>
                ))}
                <Tag onClick={() => showTagModal()}>추가하기</Tag>
            </Grid>
            {tagModalVisible ? <TagModal showTagModal={showTagModal} setTags={setTags} /> : null}
        </Container>
    );
};
