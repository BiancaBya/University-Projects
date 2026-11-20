import axios from 'axios';
import {authConfig, authMultipartConfig, baseUrl, getLogger, withLogs} from '../../core';
import { CarProps } from "./CarProps";

const carUrl = `http://${baseUrl}/api/cars`;


export const getCars: (token: string) => Promise<CarProps[]> = token => {
    return withLogs(axios.get(carUrl, authConfig(token)), 'getCars');
}

export const createCar: (token: string, car: CarProps) => Promise<CarProps[]> = (token, car) => {
    return withLogs(axios.post(carUrl, car, authConfig(token)), 'createCar');
}

export const updateCar: (token: string, car: CarProps) => Promise<CarProps[]> = (token, car) => {
    return withLogs(axios.put(`${carUrl}/${car.id}`, car, authConfig(token)), 'updateCar');
}

export const deleteCarApi: (token: string, id: number) => Promise<void> = (token, id) => {
    return withLogs(axios.delete(`${carUrl}/${id}`, authConfig(token)), 'deleteCar');
}


export const uploadCarPhoto = (token: string, id: number, file: Blob) => {
    const form = new FormData();
    form.append('file', file);
    return withLogs(
        axios.post(`${carUrl}/${id}/photo`, form, {
            ...authMultipartConfig(token),
        }),
        'uploadCarPhoto'
    ).then(r => r.data);
};



