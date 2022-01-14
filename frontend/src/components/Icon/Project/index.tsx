import React, { ChangeEvent, FC, useEffect, useRef } from 'react';

import EmptySrc from '../../../../public/assets/images/empty.png';
import { Container, FileInput, Image } from './style';

interface PropType {
    thumbnail: File | null;
    // eslint-disable-next-line no-unused-vars
    setThumbnail: (image: File) => void;
}

const ProjectIcon: FC<PropType> = ({ thumbnail, setThumbnail }) => {
    const inputRef = useRef<HTMLInputElement | null>(null);
    const imgRef = useRef<HTMLImageElement | null>(null);

    const handleImage = (event: ChangeEvent) => {
        setThumbnail((event.target as HTMLInputElement)!.files![0]);
    };

    const onClickFileInput = (event: React.MouseEvent) => {
        event.stopPropagation();
        inputRef.current?.click();
    };

    const previewImage = () => {
        if (!thumbnail) {
            imgRef.current!.src = EmptySrc;
            return;
        }

        const fileReader = new FileReader();
        fileReader.onload = () => {
            imgRef.current!.src = fileReader.result as string;
        };
        fileReader.readAsDataURL(thumbnail);
    };

    useEffect(() => {
        previewImage();
    });

    return (
        <div>
            <Container onClick={onClickFileInput}>
                <FileInput type="file" name="thumbnail" ref={inputRef} onChange={handleImage} />
                <Image ref={imgRef} />
            </Container>
        </div>
    );
};

export default ProjectIcon;
