import {
    IonButton,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonList,
    IonLoading,
    IonPage,
    IonSelect,
    IonSelectOption,
    IonTitle,
    IonToolbar
} from '@ionic/react';
import React, { useContext, useEffect, useMemo, useState } from 'react';
import { getLogger } from '../../core';
import {add, closeCircle, search} from 'ionicons/icons';
import Car from './Car';
import { RouteComponentProps } from 'react-router';
import { CarContext } from './CarProvider';
import { AuthContext } from '../../auth';
import { CarProps } from './CarProps';
import {useAppState} from "../../network/useAppState";
import {useNetwork} from "../../network/useNetwork";

import { createAnimation } from '@ionic/react';
import { useRef } from 'react';
import CustomAnimation from "../../animations/CustomAnimation";


const log = getLogger('CarList');

const PAGE_SIZE = 10;
const FIXED_FILTERS = ['Lamborghini', 'Ferrari', 'Huracan'];

const CarList: React.FC<RouteComponentProps> = ({ history }) => {
    const { logout } = useContext(AuthContext);
    const { cars, fetching, fetchingError } = useContext(CarContext);
    const { pendingCount, unsentIds, retrySync } = useContext(CarContext) as any;

    const { appState } = useAppState();
    const { networkStatus } = useNetwork();

    const [filter, setFilter] = useState<string | undefined>(undefined);
    const [visible, setVisible] = useState<CarProps[]>([]);
    const [disableInfiniteScroll, setDisableInfiniteScroll] = useState<boolean>(false);
    const [cursor, setCursor] = useState<number>(0);

    const listRef = useRef<HTMLIonListElement | null>(null);
    const [showAnimModal, setShowAnimModal] = useState(false);


    const filteredAll: CarProps[] = useMemo(() => {
        if (!cars) return [];
        if (!filter) return cars;
        return cars.filter(c => c.model?.toLowerCase().includes(filter.toLowerCase()));
    }, [cars, filter]);

    useEffect(() => {
        const start = 0;
        const end = Math.min(PAGE_SIZE, filteredAll.length);
        setVisible(filteredAll.slice(start, end));
        setTimeout(() => {
            if (listRef.current) {
                const animation = createAnimation()
                    .addElement(listRef.current)
                    .duration(700)
                    .easing('ease-in-out')
                    .fromTo('opacity', '0', '1')
                    .fromTo('transform', 'translateY(20px)', 'translateY(0)');
                animation.play();
            }
        }, 100);

        setCursor(end);
        setDisableInfiniteScroll(end >= filteredAll.length || end === 0);
    }, [filteredAll]);

    async function searchNext(e: CustomEvent<void>) {
        if (disableInfiniteScroll || fetching) {
            (e.target as HTMLIonInfiniteScrollElement).complete();
            return;
        }

        const start = cursor;
        const end = Math.min(cursor + PAGE_SIZE, filteredAll.length);
        const nextChunk = filteredAll.slice(start, end);

        setVisible(prev => [...prev, ...nextChunk]);
        const reachedEnd = end >= filteredAll.length || nextChunk.length === 0;
        setDisableInfiniteScroll(reachedEnd);
        setCursor(end);

        setTimeout(() => {
            (e.target as HTMLIonInfiniteScrollElement).complete();
        }, 250);
    }

    log('render', { fetching, shown: visible.length, total: filteredAll.length, cursor, filter });

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>Lamborghini Dealership</IonTitle>
                    <div slot="end" style={{ paddingRight: 8, fontSize: 12, display: 'flex', justifyContent: 'space-between' }}>
                        <div style={{marginRight: 30}}>App state is {JSON.stringify(appState)}</div>
                        <div>Network status is {JSON.stringify(networkStatus)}</div>
                    </div>
                    <IonButton slot="end" fill="clear" onClick={() => history.push('/search')}>
                        <IonIcon icon={search} slot="start" />
                        Search
                    </IonButton>
                    <IonButton slot="end" fill="clear" onClick={logout}>
                        Logout
                    </IonButton>
                </IonToolbar>
            </IonHeader>

            <IonContent fullscreen>
                <IonHeader collapse="condense">
                    <IonToolbar>
                        <IonTitle size="large">My Cars</IonTitle>
                    </IonToolbar>
                </IonHeader>

                <IonLoading isOpen={fetching} message="Fetching cars" />
                <div style={{ display: 'flex', gap: 8, alignItems: 'center', padding: 12 }}>
                    <IonSelect
                        value={filter}
                        placeholder="Select brand"
                        onIonChange={e => {
                            const v = e.detail.value as string | undefined;
                            setFilter(v === '__ALL__' ? undefined : v);
                        }}
                        interface="popover"
                    >
                        <IonSelectOption value="__ALL__">All / No filter</IonSelectOption>
                        {FIXED_FILTERS.map(f => (
                            <IonSelectOption key={f} value={f}>
                                {f}
                            </IonSelectOption>
                        ))}
                    </IonSelect>
                    <IonButton
                        size="small"
                        fill="clear"
                        onClick={() => setFilter(undefined)}
                        disabled={filter === undefined}
                    >
                        <IonIcon icon={closeCircle} slot="start" />
                        Clear
                    </IonButton>
                </div>

                {(pendingCount ?? 0) > 0 && (
                    <div style={{
                        margin: 12, padding: 12,
                        border: '1px solid var(--ion-color-warning)',
                        background: 'rgba(255, 199, 0, 0.15)',
                        borderRadius: 12, display: 'flex',
                        alignItems: 'center', justifyContent: 'space-between',
                        gap: 12
                    }}>
                        <div>
                            {pendingCount} cars{pendingCount > 1 ? 'i' : ''} waiting.
                            {networkStatus?.connected
                                ? ' Sending data to server...'
                                : ' You are offline.'}
                        </div>
                        <IonButton size="small" onClick={() => retrySync?.()} disabled={!networkStatus?.connected}>
                            Retry
                        </IonButton>
                    </div>
                )}

                <IonButton style={{padding: 20}} onClick={() => setShowAnimModal(true)}>
                    🏎️
                </IonButton>
                <CustomAnimation isOpen={showAnimModal} onClose={() => setShowAnimModal(false)} />


                {visible && (
                    <IonList ref={listRef} style={{ minHeight: '120vh' }}>
                        {visible.map(({ id, model, manufacturingDate, owned, photoUrl, photoPath }) => (
                            <Car
                                key={id}
                                id={id}
                                model={model}
                                manufacturingDate={manufacturingDate}
                                owned={owned}
                                photoUrl={photoUrl}
                                photoPath={photoPath}
                                onEdit={id => history.push(`/car/${id}`)}
                            />
                        ))}
                    </IonList>
                )}

                {(!fetching && visible.length === 0) && (
                    <div style={{ padding: 12 }}>No cars found.</div>
                )}

                {fetchingError && <div style={{ padding: 12 }}>{fetchingError.message || 'Failed to fetch cars'}</div>}

                <IonFab vertical="bottom" horizontal="end" slot="fixed">
                    <IonFabButton onClick={() => history.push('/car')}>
                        <IonIcon icon={add} />
                    </IonFabButton>
                </IonFab>

                <IonInfiniteScroll
                    threshold="100px"
                    disabled={disableInfiniteScroll}
                    onIonInfinite={(e: CustomEvent<void>) => searchNext(e)}
                >
                    <IonInfiniteScrollContent loadingText="Loading more cars..." />
                </IonInfiniteScroll>
            </IonContent>
        </IonPage>
    );
};

export default CarList;
