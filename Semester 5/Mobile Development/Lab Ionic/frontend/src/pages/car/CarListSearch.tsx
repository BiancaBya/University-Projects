import {
    IonButton,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonList,
    IonLoading,
    IonPage,
    IonSearchbar,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import React, { useContext, useState } from 'react';
import { getLogger } from '../../core';
import { add } from 'ionicons/icons';
import Car from './Car';
import { RouteComponentProps } from 'react-router';
import { CarContext } from './CarProvider';
import { AuthContext } from '../../auth';

const log = getLogger('CarListSearch');

const CarListSearch: React.FC<RouteComponentProps> = ({ history }) => {
    const { logout } = useContext(AuthContext);
    const { cars, fetching, fetchingError } = useContext(CarContext);

    const [searchModel, setSearchModel] = useState<string>('');

    log('render', { fetching, searchModel });

    const filtered = (cars || []).filter(c => (c.model || '').indexOf(searchModel) >= 0);

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Search Cars</IonTitle>
                    <IonButton slot="end" fill="clear" onClick={logout}>
                        Logout
                    </IonButton>
                </IonToolbar>
            </IonHeader>

            <IonContent fullscreen>
                <IonHeader collapse="condense">
                    <IonToolbar>
                        <IonTitle size="large">Search Cars</IonTitle>
                    </IonToolbar>
                </IonHeader>

                <IonLoading isOpen={fetching} message="Fetching cars" />

                <IonSearchbar
                    value={searchModel}
                    debounce={1000}
                    onIonInput={e => setSearchModel(e.detail.value!)}
                    placeholder="Search by model..."
                />

                {filtered && (
                    <IonList>
                        {filtered.map(({ id, model, manufacturingDate, owned }) => (
                            <Car
                                key={id}
                                id={id}
                                model={model}
                                manufacturingDate={manufacturingDate}
                                owned={owned}
                                onEdit={id => history.push(`/car/${id}`)}
                            />
                        ))}
                    </IonList>
                )}

                {(!fetching && filtered.length === 0) && (
                    <div style={{ padding: 12 }}>No cars found.</div>
                )}

                {fetchingError && (
                    <div style={{ padding: 12 }}>{fetchingError.message || 'Failed to fetch cars'}</div>
                )}

                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/car')}>
                        <IonIcon icon={add} />
                    </IonFabButton>
                </IonFab>
            </IonContent>
        </IonPage>
    );
};

export default CarListSearch;
