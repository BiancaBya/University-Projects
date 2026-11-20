import React from 'react';
import { IonModal, IonButton, IonHeader, IonToolbar, IonTitle, IonContent } from '@ionic/react';
import MyMap from './MyMap';

interface MapViewModalProps {
    isOpen: boolean;
    onClose: () => void;
    lat?: number;
    lng?: number;
}

const MapViewModal: React.FC<MapViewModalProps> = ({ isOpen, onClose, lat, lng }) => {
    return (
        <IonModal isOpen={isOpen} onDidDismiss={onClose}>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Car Location</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent>
                {lat && lng ? (
                    <MyMap
                        lat={lat}
                        lng={lng}
                        onMapClick={() => {}}
                        onMarkerClick={() => {}}
                    />
                ) : (
                    <div style={{ padding: 16 }}>No location set for this car.</div>
                )}
                <div style={{ padding: 16 }}>
                    <IonButton onClick={onClose}>Close</IonButton>
                </div>
            </IonContent>
        </IonModal>
    );
};

export default MapViewModal;


