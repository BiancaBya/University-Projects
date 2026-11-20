import React, { useCallback, useContext, useEffect, useReducer } from "react";
import { getLogger } from "../../core";
import { CarProps } from "./CarProps";
import { getCars, createCar, updateCar, deleteCarApi} from "./CarApi";
import { AuthContext } from "../../auth";
import { useNetwork } from "../../network/useNetwork";

import { useFilesystem } from "../../photos/useFilesystem";
import { usePreferences } from '../../network/usePreferences';

import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const log = getLogger("useCars");

type SaveCarFn = (car: CarProps) => Promise<any>;
type DeleteCarFn = (id: number) => Promise<any>;

export interface CarsState {
    cars?: CarProps[];
    fetching: boolean;
    fetchingError?: Error | null;
    saving: boolean;
    savingError?: Error | null;
    deleting: boolean;
    deletingError?: Error | null;
    saveCar?: SaveCarFn;
    deleteCar?: DeleteCarFn;

    pendingCount?: number;
    unsentIds?: number[];
}

interface ActionProps {
    type: string;
    payload?: any;
}

const initialState: CarsState = {
    fetching: false,
    saving: false,
    deleting: false,
    pendingCount: queueSize(),
    unsentIds: [],
};

const LS_QUEUE_KEY = 'cars.pendingQueue';
type PendingOp =
    | { op: 'create'; car: CarProps }
    | { op: 'update'; car: CarProps }
    | { op: 'delete'; id: number };

function readQueue(): PendingOp[] {
    try { return JSON.parse(localStorage.getItem(LS_QUEUE_KEY) || '[]'); }
    catch { return []; }
}
function writeQueue(q: PendingOp[]) {
    localStorage.setItem(LS_QUEUE_KEY, JSON.stringify(q));
}
function enqueue(op: PendingOp) {
    const q = readQueue();
    q.push(op);
    writeQueue(q);
}
function dequeue(): PendingOp | undefined {
    const q = readQueue();
    const op = q.shift();
    writeQueue(q);
    return op;
}
function clearQueue() { writeQueue([]); }
function queueSize() { return readQueue().length; }

const FETCH_CARS_STARTED = "FETCH_CARS_STARTED";
const FETCH_CARS_SUCCEEDED = "FETCH_CARS_SUCCEEDED";
const FETCH_CARS_FAILED = "FETCH_CARS_FAILED";
const SAVE_CARS_STARTED = "SAVE_CARS_STARTED";
const SAVE_CARS_SUCCEEDED = "SAVE_CARS_SUCCEEDED";
const SAVE_CARS_FAILED = "SAVE_CARS_FAILED";
const DELETE_CARS_STARTED = "DELETE_CARS_STARTED";
const DELETE_CARS_SUCCEEDED = "DELETE_CARS_SUCCEEDED";
const DELETE_CARS_FAILED = "DELETE_CARS_FAILED";
const INSERT = "INSERT_CAR";
const UPDATE = "UPDATE_CAR";
const DELETE = "DELETE_CAR";
const PENDING_MARK = "PENDING_MARK";
const PENDING_CLEAR = "PENDING_CLEAR";
const PENDING_COUNT = "PENDING_COUNT";

const reducer: (state: CarsState, action: ActionProps) => CarsState = (state, { type, payload }) => {
    switch (type) {
        case FETCH_CARS_STARTED:
            return { ...state, fetching: true, fetchingError: null };

        case FETCH_CARS_SUCCEEDED:
            return {
                ...state,
                cars: payload.cars,
                fetching: false,
                savingError: null,
                fetchingError: null,
            };

        case FETCH_CARS_FAILED:
            return {
                ...state,
                fetching: false,
                fetchingError: payload.error,
            };

        case SAVE_CARS_STARTED:
            return { ...state, saving: true, savingError: null };

        case SAVE_CARS_SUCCEEDED:
            const cars = [...(state.cars || [])];
            const car = payload.savedCar;
            const index = cars.findIndex((carInList) => carInList.id === car.id);
            if (index === -1) {
                cars.splice(0, 0, car);
            } else {
                cars[index] = car;
            }
            return { ...state, cars, saving: false, fetching: false };

        case SAVE_CARS_FAILED:
            return { ...state, saving: false, savingError: payload.error };

        case DELETE_CARS_STARTED:
            return { ...state, deleting: true, deletingError: null, fetching: true };

        case DELETE_CARS_SUCCEEDED:
            const id = payload?.id;
            console.log(id);
            console.log(state.cars)
            return {
                ...state,
                cars: (state.cars || []).filter((d) => d.id !== id),
                deleting: false,
                deletingError: null,
                fetching: false,
            };

        case DELETE_CARS_FAILED:
            return { ...state, deleting: false, deletingError: payload.error };

        case INSERT:
            const newDest = payload.car;
            const carList = state.cars ? [...state.cars] : [];
            carList.splice(0, 0, newDest);
            return {
                ...state,
                cars: carList
            };

        case UPDATE:
            const d = payload.car as CarProps;
            const currentList = [...(state.cars || [])];
            const idx = currentList.findIndex(x => x.id === d.id);
            currentList[idx] = d;
            return { ...state, cars: currentList };

        case DELETE:
            console.log(payload);
            const deletedId = payload.id as number;
            const list = (state.cars || []).filter(x => x.id !== deletedId);
            return { ...state, cars: list };

        case PENDING_MARK: {
            const { ids } = payload as { ids: number[] };
            return { ...state, unsentIds: Array.from(new Set([ ...(state.unsentIds || []), ...ids ])) };
        }
        case PENDING_CLEAR: {
            const { ids } = payload as { ids: number[] };
            const rest = (state.unsentIds || []).filter(id => !ids.includes(id));
            return { ...state, unsentIds: rest };
        }
        case PENDING_COUNT:
            return { ...state, pendingCount: payload.count };


        default:
            return state;
    }
};

export const CarContext = React.createContext<CarsState>(initialState);

interface CarProviderProps {
    children: React.ReactNode;
}

const CAR_PHOTOS = 'CAR_PHOTOS';


export const CarProvider: React.FC<CarProviderProps> = ({ children, }) => {
    const { token } = useContext(AuthContext);
    const { networkStatus } = useNetwork();
    const [state, dispatch] = useReducer(reducer, initialState);
    const {
        cars,
        fetching,
        fetchingError,
        saving,
        savingError,
        deleting,
        deletingError,
        pendingCount,
        unsentIds,
    } = state;

    useEffect(getCarsEffect, [token]);
    useEffect(() => connectWs(), [token]);
    useEffect(() => {
        if (!token) return;
        if (networkStatus?.connected) {
            void processQueue();
        }
    }, [networkStatus?.connected, token]);

    const saveCar = useCallback<SaveCarFn>(saveCarCallback, [token, networkStatus?.connected]);
    const deleteCar = useCallback<DeleteCarFn>(deleteCarCallback, [token, networkStatus?.connected]);

    const value = {
        cars,
        fetching,
        fetchingError,
        saving,
        savingError,
        saveCar,
        deleting,
        deletingError,
        deleteCar,
        pendingCount,
        unsentIds,
        retrySync: processQueue,
    };

    const { readFile } = useFilesystem();
    const { get, set } = usePreferences();


    async function readPhotoMap(): Promise<Record<number, { path: string }>> {
        const s = (await get(CAR_PHOTOS)) ?? '{}';
        try { return JSON.parse(s); } catch { return {}; }
    }

    async function mergeLocalPhotos(cars: CarProps[]) {
        const map = await readPhotoMap();
        const merged: CarProps[] = [];
        for (const c of cars) {
            const path = c.id ? map[c.id]?.path : undefined;
            let photoUrl = c.photoUrl;
            if (path && !photoUrl) {
                try {
                    const base64 = await readFile(path);
                    photoUrl = `data:image/jpeg;base64,${base64}`;
                } catch { }
            }
            merged.push({ ...c, photoPath: path ?? c.photoPath, photoUrl });
        }
        return merged;
    }

    function getCarsEffect() {
        let cancelled = false;
        if (token) {
            fetchData();
        }
        return () => {
            cancelled = true;
        };

        async function fetchData() {
            try {
                log("fetchData - started");
                dispatch({ type: FETCH_CARS_STARTED });
                const cars = await getCars(token);
                const merged = await mergeLocalPhotos(cars);
                if (!cancelled) {
                    log("fetchData - finished");
                    dispatch({
                        type: FETCH_CARS_SUCCEEDED,
                        payload: { cars: merged },
                    });
                }
            } catch (error) {
                if (!cancelled) {
                    log("fetchData - failed", error);
                    dispatch({ type: FETCH_CARS_FAILED, payload: { error } });
                }
            }
        }
    }

    function tempId() {
        return -Date.now();
    }

    async function processQueue() {
        if (!token) return;
        let op = readQueue()[0];
        if (!op) {
            dispatch({ type: PENDING_COUNT, payload: { count: 0 } });
            return;
        }

        while ((op = dequeue()!)) {
            try {
                if (op.op === 'create') {

                    const { id: localId, ...rest } = op.car;
                    const saved = await createCar(token, { ...rest, id: undefined });

                    dispatch({ type: DELETE, payload: { id: localId } });
                    dispatch({ type: SAVE_CARS_SUCCEEDED, payload: { savedCar: saved } });
                    dispatch({ type: PENDING_CLEAR, payload: { ids: [localId!] } });
                } else if (op.op === 'update') {
                    const saved = await updateCar(token, op.car);
                    dispatch({ type: SAVE_CARS_SUCCEEDED, payload: { savedCar: saved } });
                    dispatch({ type: PENDING_CLEAR, payload: { ids: [op.car.id!] } });
                } else if (op.op === 'delete') {
                    await deleteCarApi(token, op.id);

                    dispatch({ type: PENDING_CLEAR, payload: { ids: [op.id] } });
                }
            } catch (e) {

                const q = readQueue();
                q.push(op);
                writeQueue(q);
                break;
            }
        }
        dispatch({ type: PENDING_COUNT, payload: { count: queueSize() } });
    }


    async function saveCarCallback(car: CarProps) {
        const online = !!networkStatus?.connected;
        try {
            dispatch({ type: SAVE_CARS_STARTED });

            if (online) {
                const savedCar = await (!car.id ? createCar(token, car) : updateCar(token, car));
                dispatch({ type: SAVE_CARS_SUCCEEDED, payload: { savedCar } });
                return;
            }

            const pendingCar: CarProps = !car.id ? { ...car, id: tempId() } : { ...car };

            dispatch({ type: SAVE_CARS_SUCCEEDED, payload: { savedCar: pendingCar } });
            dispatch({ type: PENDING_MARK, payload: { ids: [pendingCar.id!] } });

            enqueue(!car.id ? { op: 'create', car: pendingCar } : { op: 'update', car: pendingCar });
            dispatch({ type: PENDING_COUNT, payload: { count: queueSize() } });
        } catch (error) {

            const fallback = (!car.id ? 'create' : 'update') as 'create' | 'update';
            const pendingCar: CarProps = !car.id ? { ...car, id: tempId() } : { ...car };

            dispatch({ type: SAVE_CARS_FAILED, payload: { error } });
            dispatch({ type: SAVE_CARS_SUCCEEDED, payload: { savedCar: pendingCar } });
            dispatch({ type: PENDING_MARK, payload: { ids: [pendingCar.id!] } });

            enqueue({ op: fallback, car: pendingCar });
            dispatch({ type: PENDING_COUNT, payload: { count: queueSize() } });
        }
    }


    async function deleteCarCallback(id: number) {
        const online = !!networkStatus?.connected;
        try {
            dispatch({ type: DELETE_CARS_STARTED });
            if (online) {
                await deleteCarApi(token, id);
                dispatch({ type: DELETE_CARS_SUCCEEDED, payload: { id } });
                return;
            }

            dispatch({ type: DELETE_CARS_SUCCEEDED, payload: { id } });
            dispatch({ type: PENDING_MARK, payload: { ids: [id] } });

            enqueue({ op: 'delete', id });
            dispatch({ type: PENDING_COUNT, payload: { count: queueSize() } });
        } catch (error) {

            dispatch({ type: DELETE_CARS_FAILED, payload: { error } });
            dispatch({ type: DELETE_CARS_SUCCEEDED, payload: { id } });
            dispatch({ type: PENDING_MARK, payload: { ids: [id] } });

            enqueue({ op: 'delete', id });
            dispatch({ type: PENDING_COUNT, payload: { count: queueSize() } });
        }
    }

    function connectWs() {
        if (!token) return;
        const client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/cars-ws'),
            connectHeaders: { Authorization: `Bearer ${token}` },
            reconnectDelay: 5000,
            debug: () => {}
        });

        client.onConnect = () => {
            log('Websocket connected');
            client.subscribe('/user/queue/notifications', message => {
                const body = JSON.parse(message.body);
                log(`Websocket message received: ${message.body}`);
                try{
                    switch(body.type) {
                        case 'WS_INSERT':
                            dispatch({ type: INSERT, payload: { car: body.car } }); break;
                        case 'WS_UPDATE':
                            dispatch({ type: UPDATE, payload: { car: body.car } }); break;
                        case 'WS_DELETE':
                            const id = body.id ?? body.car?.id;
                            dispatch({ type: DELETE, payload: { id } }); break;
                    }
                } catch (e) {
                    log(`Websocket failed: ${e}`);
                }
            });
        };

        client.onStompError = frame => {
            log('WS error', frame.headers['message'], frame.body);
        };

        client.activate();
        return () => { client.deactivate(); }
    }

    return (
        <CarContext.Provider value={value}>
            {children}
        </CarContext.Provider>
    );
};


