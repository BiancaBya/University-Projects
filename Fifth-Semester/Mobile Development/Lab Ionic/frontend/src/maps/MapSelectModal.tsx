import React, { useState } from 'react';
import { IonModal, IonButton, IonHeader, IonToolbar, IonTitle, IonContent } from '@ionic/react';
import MyMap from '../maps/MyMap';
import { useMyLocation } from './useMyLocation';

interface MapSelectModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSelect: (lat: number, lng: number) => void;
}

const MapSelectModal: React.FC<MapSelectModalProps> = ({ isOpen, onClose, onSelect }) => {
    const { position } = useMyLocation();
    const { latitude: lat, longitude: lng } = position?.coords || {};
    const [selected, setSelected] = useState<{ lat: number, lng: number } | null>(null);

    const handleMapClick = (e: any) => setSelected({ lat: e.latitude, lng: e.longitude });

    return (
        <IonModal isOpen={isOpen} onDidDismiss={onClose}>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Select Location</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                {lat && lng && (
                    <MyMap lat={lat} lng={lng} onMapClick={handleMapClick} onMarkerClick={() => {}} />
                )}
                <div style={{ padding: 16 }}>
                    {selected && (
                        <div>
                            <p>Selected: {selected.lat.toFixed(5)}, {selected.lng.toFixed(5)}</p>
                            <IonButton onClick={() => { onSelect(selected.lat, selected.lng); onClose(); }}>
                                Confirm
                            </IonButton>
                        </div>
                    )}
                    <IonButton fill="clear" onClick={onClose}>Cancel</IonButton>
                </div>
            </IonContent>
        </IonModal>
    );
};

export default MapSelectModal;
