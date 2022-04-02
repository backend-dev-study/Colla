import React, { FC, useEffect, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { getProjectTags } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { TagModal } from '../../Modal/Tag';
import { Container, Grid, Tag } from './style';

interface PropType {
    selectedTags: Array<string>;
    handleSelectTag: Function;
}

export const TagList: FC<PropType> = ({ selectedTags, handleSelectTag }) => {
    const project = useRecoilValue(projectState);
    const [tags, setTags] = useState<Array<string>>([]);
    const [tagModalVisible, setTagModalVisible] = useState(false);

    const showTagModal = () => {
        setTagModalVisible((prev) => !prev);
    };

    const selectTag = (name: string) => {
        selectedTags.includes(name)
            ? handleSelectTag(selectedTags.filter((el) => el !== name))
            : handleSelectTag([...selectedTags, name]);
    };

    useEffect(() => {
        (async () => {
            const res = await getProjectTags(project.id);
            setTags(() => res.data.map((tag) => tag.name));
        })();
    }, []);

    return (
        <Container>
            <Grid>
                {tags.map((name, idx) => (
                    <Tag
                        key={idx}
                        className={selectedTags.includes(name) ? 'selected' : ''}
                        onClick={() => selectTag(name)}
                    >
                        {name}
                    </Tag>
                ))}
                <Tag onClick={() => showTagModal()}>추가하기</Tag>
            </Grid>
            {tagModalVisible ? <TagModal showTagModal={showTagModal} setTags={setTags} /> : null}
        </Container>
    );
};
