import React, { FC, useState } from 'react';

import { Container, Grid, Tag } from './style';

interface PropType {
    tags: Array<string>;
    showTagModal: Function;
}

export const TagList: FC<PropType> = ({ tags, showTagModal }) => {
    const [selectedTags, setSelectedTags] = useState<Array<number>>([]);

    const selectTag = (idx: number) => {
        selectedTags.includes(idx)
            ? setSelectedTags((prev) => prev.filter((el) => el !== idx))
            : setSelectedTags((prev) => [...prev, idx]);
    };

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
        </Container>
    );
};
