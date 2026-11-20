import React from 'react';
import { IonModal, IonButton, createAnimation } from '@ionic/react';

interface ConfirmModalProps {
    isOpen: boolean;
    onClose: () => void;
}

const CustomAnimation: React.FC<ConfirmModalProps> = ({ isOpen, onClose }) => {
    const enterAnimation = (baseEl: any) => {
        const root = baseEl.shadowRoot;
        const backdropAnimation = createAnimation()
            .addElement(root.querySelector('ion-backdrop')!)
            .fromTo('opacity', '0.01', 'var(--backdrop-opacity)');

        const wrapperAnimation = createAnimation()
            .addElement(root.querySelector('.modal-wrapper')!)
            .keyframes([
                { offset: 0, opacity: '0', transform: 'scale(0.3)' },
                { offset: 1, opacity: '1', transform: 'scale(1)' },
            ]);

        return createAnimation()
            .addElement(baseEl)
            .easing('ease-out')
            .duration(400)
            .addAnimation([backdropAnimation, wrapperAnimation]);
    };

    const leaveAnimation = (baseEl: any) => enterAnimation(baseEl).direction('reverse');

    return (
        <IonModal isOpen={isOpen} enterAnimation={enterAnimation} leaveAnimation={leaveAnimation}>
            <div style={{ padding: 24, textAlign: 'center' }}>
                <h2>Enjoy using the App! 🏎️</h2>
                <IonButton onClick={onClose}>Close</IonButton>
            </div>
        </IonModal>
    );
};

export default CustomAnimation;
