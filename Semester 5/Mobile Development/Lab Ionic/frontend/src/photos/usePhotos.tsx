import { useEffect, useState } from 'react';
import { useCamera } from './useCamera';
import { useFilesystem } from './useFilesystem';
import { usePreferences } from '../network/usePreferences';

export interface MyPhoto {
    filepath: string;
    webviewPath?: string;
}

const PHOTOS = 'photos';

export function usePhotos() {
    const [photos, setPhotos] = useState<MyPhoto[]>([]);
    const { getPhoto } = useCamera();
    const { readFile, writeFile, deleteFile } = useFilesystem();
    const { get, set } = usePreferences();

    useEffect(() => {
        loadSavedPhotos();
        async function loadSavedPhotos() {
            const saved = (await get(PHOTOS)) ?? '[]';
            const savedFiles = JSON.parse(saved) as { filepath: string }[];

            const restored: MyPhoto[] = [];
            for (const { filepath } of savedFiles) {
                const base64 = await readFile(filepath);
                restored.push({
                    filepath,
                    webviewPath: `data:image/jpeg;base64,${base64}`,
                });
            }
            setPhotos(restored);
        }
    }, [get, readFile]);

    return {
        photos,
        takePhoto,
        deletePhoto,
    };

    async function takePhoto(): Promise<MyPhoto> {
        const data = await getPhoto();
        const filepath = `${Date.now()}.jpeg`;

        await writeFile(filepath, data.base64String!);

        const newPhoto: MyPhoto = {
            filepath,
            webviewPath: `data:image/jpeg;base64,${data.base64String}`,
        };

        const newPhotos = [newPhoto, ...photos];
        setPhotos(newPhotos);
        await set(
            PHOTOS,
            JSON.stringify(newPhotos.map(p => ({ filepath: p.filepath })))
        );

        return newPhoto;
    }

    async function deletePhoto(photo: MyPhoto) {
        const newPhotos = photos.filter(p => p.filepath !== photo.filepath);
        setPhotos(newPhotos);
        await deleteFile(photo.filepath);
        await set(
            PHOTOS,
            JSON.stringify(newPhotos.map(p => ({ filepath: p.filepath })))
        );
    }
}
