package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
    }

    // Create a new reservation
    public boolean createReservation(Reservation reservation) {
        // Business logic before saving the reservation
        if (isValidReservation(reservation)) {
            return reservationDAO.createReservation(reservation);
        }
        return false;
    }

    // Update an existing reservation
    public boolean updateReservation(Reservation reservation) {
        // Business logic before updating the reservation
        if (isValidReservation(reservation)) {
            return reservationDAO.updateReservation(reservation);
        }
        return false;
    }

    // Cancel a reservation
    public boolean cancelReservation(int reservationId) {
        // Business logic for canceling a reservation
        return reservationDAO.cancelReservation(reservationId);
    }

    // Get reservation by ID
    public Reservation getReservationById(int reservationId) {
        return reservationDAO.getReservationById(reservationId);
    }

    // Get all reservations with optional filtering
    public List<Reservation> getAllReservations(String filter, String filterValue) {
        return reservationDAO.getAllReservations(filter, filterValue);
    }

    // Check if the reservation is valid (business rule example)
    private boolean isValidReservation(Reservation reservation) {
        // Business logic validation, e.g., check availability, validate dates, etc.
        if (reservation.getCheckIn().before(reservation.getCheckOut())) {
            return true;
        }
        return false;
    }
}