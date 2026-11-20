export interface CarProps {
    id?: number;
    model: string;
    manufacturingDate: Date;
    owned: boolean;

    photoUrl?: string;
    photoPath?: string;

    lat?: number;
    lng?: number;
}

