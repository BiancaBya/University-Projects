package services;

import org.example.Domain.Reservation;

import java.io.IOException;

public interface IObserver {
    public void reservationAdded(Reservation reservation) throws ServiceException;
}



