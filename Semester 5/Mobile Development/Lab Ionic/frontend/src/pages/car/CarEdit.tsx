import React, { useContext, useEffect, useState } from 'react';
import {
    IonButton,
    IonButtons,
    IonCheckbox,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonImg,
    IonInput,
    IonItem,
    IonLoading,
    IonPage,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import { getLogger } from '../../core';
import { CarContext } from './CarProvider';
import { RouteComponentProps } from 'react-router';
import { CarProps } from './CarProps';
import { invertMode } from 'ionicons/icons';

import { usePhotos, MyPhoto } from '../../photos/usePhotos';
import { useFilesystem } from '../../photos/useFilesystem';
import { usePreferences} from "../../network/usePreferences";
import { AuthContext } from '../../auth';
import { uploadCarPhoto } from './CarApi';

import MapSelectModal from '../../maps/MapSelectModal';
import MapViewModal from '../../maps/MapViewModal';



const log = getLogger('CarEdit');

const CAR_PHOTOS = 'CAR_PHOTOS';
const CAR_LOCATIONS = 'CAR_LOCATIONS';

interface CarEditProps extends RouteComponentProps<{ id?: string }> {}

const CarEdit: React.FC<CarEditProps> = ({ history, match }) => {
    const { cars, saving, savingError, saveCar, deleteCar } = useContext(CarContext);

    const [model, setModel] = useState('');
    const [manufacturingDate, setManufacturingDate] = useState(new Date());
    const [owned, setOwned] = useState(false);
    const [car, setCar] = useState<CarProps | undefined>(undefined);

    const { token } = useContext(AuthContext);
    const [photoIsNew, setPhotoIsNew] = useState(false);

    const [photoPreview, setPhotoPreview] = useState<string | undefined>(undefined);
    const [photoPath, setPhotoPath] = useState<string | undefined>(undefined);

    const { takePhoto } = usePhotos();
    const { readFile } = useFilesystem();
    const { get, set } = usePreferences();

    const [lat, setLat] = useState<number | undefined>(undefined);
    const [lng, setLng] = useState<number | undefined>(undefined);
    const [isMapOpen, setIsMapOpen] = useState(false);
    const [isMapViewOpen, setIsMapViewOpen] = useState(false);


    useEffect(() => {
        log('useEffect');
        const routeId = match.params.id || '';
        const found = cars?.find(it => it.id?.toString() === routeId);
        setCar(found);

        if (found) {
            setModel(found.model);
            setManufacturingDate(new Date(found.manufacturingDate));
            setOwned(found.owned);
            if (found.photoUrl) {
                setPhotoPreview(found.photoUrl);
            } else {
                loadLocalPhoto(found.id!).catch(() => setPhotoPreview(undefined));
            }
            setLat(found.lat);
            setLng(found.lng);
            loadLocalLocation(found.id!).catch(() => {
                setLat(undefined);
                setLng(undefined);
            });

        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [match.params.id, cars]);

    async function loadLocalLocation(carId: number) {
        const locStr = (await get(CAR_LOCATIONS)) ?? '{}';
        const map = JSON.parse(locStr) as Record<number, { lat: number; lng: number }>;
        const entry = map[carId];
        if (!entry) return;
        setLat(entry.lat);
        setLng(entry.lng);
    }


    function dataUrlToBlob(dataUrl: string): Blob {
        const [meta, b64] = dataUrl.split(',');
        const mime = /data:(.*?);base64/.exec(meta)?.[1] || 'image/jpeg';
        const bin = atob(b64);
        const bytes = new Uint8Array(bin.length);
        for (let i = 0; i < bin.length; i++) bytes[i] = bin.charCodeAt(i);
        return new Blob([bytes], { type: mime });
    }

    async function readLocalAsDataUrl(path: string) {
        const base64 = await readFile(path);
        return `data:image/jpeg;base64,${base64}`;
    }


    async function loadLocalPhoto(carId: number) {
        const mapStr = (await get(CAR_PHOTOS)) ?? '{}';
        const map = JSON.parse(mapStr) as Record<number, { path: string }>;
        const entry = map[carId];
        if (!entry?.path) return;
        setPhotoPath(entry.path);

        const base64 = await readFile(entry.path);
        setPhotoPreview(`data:image/jpeg;base64,${base64}`);
    }

    async function onTakePhoto() {
        const newPhoto: MyPhoto = await takePhoto();
        setPhotoPreview(newPhoto.webviewPath);
        setPhotoPath(newPhoto.filepath);
        setPhotoIsNew(true);
    }

    const handleSave = async () => {
        const editedCar: CarProps = car
            ? { ...car, model, manufacturingDate, owned, photoPath, lat, lng }
            : { model, manufacturingDate, owned, photoPath, lat, lng };

        const saved = await saveCar?.(editedCar);
        const savedCar: CarProps = saved ?? { ...(car ?? {}), ...editedCar };

        const id = savedCar.id ?? car?.id ?? editedCar.id;
        if (id && photoPath) {
            const mapStr = (await get(CAR_PHOTOS)) ?? '{}';
            const map = JSON.parse(mapStr) as Record<number, { path: string }>;
            map[id] = { path: photoPath };
            await set(CAR_PHOTOS, JSON.stringify(map));
        }

        if (token && id && photoIsNew && photoPath) {
            const dataUrl = await readLocalAsDataUrl(photoPath);
            const blob = dataUrlToBlob(dataUrl);
            try {
                const updatedWithPhoto: CarProps = await uploadCarPhoto(token, id, blob);
                setCar(updatedWithPhoto);
                setPhotoPreview(updatedWithPhoto.photoUrl ? `${updatedWithPhoto.photoUrl}?v=${Date.now()}` : photoPreview);
            } catch (e) {
                console.warn('Photo upload failed', e);
            } finally {
                setPhotoIsNew(false);
            }
        }

        if (id && lat && lng) {
            const locStr = (await get(CAR_LOCATIONS)) ?? '{}';
            const map = JSON.parse(locStr) as Record<number, { lat: number; lng: number }>;
            map[id] = { lat, lng };
            await set(CAR_LOCATIONS, JSON.stringify(map));
        }

        history.goBack();
    };

    const handleDelete = () => {
        const id = car?.id ?? -1;
        deleteCar?.(id).then(() => history.goBack());
    };

    log('render');
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Edit</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={handleSave}>Save</IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>

            <IonContent>
                <IonItem>
                    <IonInput
                        value={model}
                        onIonChange={e => setModel(e.detail.value ?? '')}
                    />
                </IonItem>

                <IonItem>
                    <IonInput
                        type="date"
                        value={manufacturingDate.toISOString().substring(0, 10)}
                        placeholder="Date"
                        onIonChange={(e) =>
                            setManufacturingDate(
                                new Date(e.detail.value || new Date().toISOString())
                            )
                        }
                    />
                </IonItem>

                <IonItem>
                    <IonCheckbox
                        labelPlacement="end"
                        justify="start"
                        checked={owned}
                        onIonChange={(e) => setOwned(e.detail.checked)}
                    >
                        Owned
                    </IonCheckbox>
                </IonItem>

                <IonButton onClick={onTakePhoto}>Take photo</IonButton>
                {photoPreview && (
                    <div style={{ padding: 12 }}>
                        <IonImg src={photoPreview} />
                    </div>
                )}

                <IonButton onClick={() => setIsMapOpen(true)}>Select location</IonButton>
                <IonButton
                    onClick={() => setIsMapViewOpen(true)}
                    disabled={!lat || !lng}
                >
                    View location
                </IonButton>
                {lat && lng && (
                    <div style={{ padding: 12 }}>
                        Current location: {lat.toFixed(5)}, {lng.toFixed(5)}
                    </div>
                )}
                <MapSelectModal
                    isOpen={isMapOpen}
                    onClose={() => setIsMapOpen(false)}
                    onSelect={(la, ln) => { setLat(la); setLng(ln); }}
                />
                <MapViewModal
                    isOpen={isMapViewOpen}
                    onClose={() => setIsMapViewOpen(false)}
                    lat={lat}
                    lng={lng}
                />


                <IonLoading isOpen={saving} />
                {savingError && <div>{savingError.message || 'Failed to save car'}</div>}

                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={handleDelete}>
                        <IonIcon icon={invertMode} />
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default CarEdit;


