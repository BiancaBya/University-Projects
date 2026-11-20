import React from 'react';
import { IonItem, IonLabel } from '@ionic/react';
import { getLogger} from "../../core";
import { CarProps } from "./CarProps";

import { IonAvatar, IonThumbnail, IonImg} from '@ionic/react';
import { Capacitor } from '@capacitor/core';

const log = getLogger('Item');

interface CarPropsExt extends CarProps {
    onEdit: (id?: number) => void;
}

const Car: React.FC<CarPropsExt> = ({ id, model, manufacturingDate, owned, photoUrl, photoPath, onEdit }) => {
    log(`render ${model}, ${manufacturingDate}, ${owned}`);

    const localSrc = photoPath ? Capacitor.convertFileSrc(`file://${photoPath}`) : undefined;
    const src = photoUrl || localSrc;

    return (
        <IonItem onClick={() => onEdit(id)}>
            {src && (
                <IonThumbnail slot="start">
                    <IonImg src={src} />
                </IonThumbnail>
            )}
            <IonLabel>{model}</IonLabel>
        </IonItem>
    );
};

export default Car;

