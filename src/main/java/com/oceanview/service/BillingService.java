package com.oceanview.service;

import com.oceanview.dao.BillingDAO;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;

import java.util.Date;

public class BillingService {

    private final BillingDAO billingDAO;

    public BillingService() {
        this.billingDAO = new BillingDAO();
    }

    // Generate a bill based on a reservation
    public Bill generateBill(Reservation reservation) {
        double totalAmount = calculateTotalAmount(reservation);
        Bill bill = new Bill(reservation.getReservationId(), totalAmount, new Date(), "UNPAID");
        return bill;
    }

    // Save the generated bill to the database
    public boolean saveBill(Bill bill) {
        return billingDAO.saveBill(bill);
    }

    // Retrieve a bill by reservation ID
    public Bill getBillByReservationId(int reservationId) {
        return billingDAO.getBillByReservationId(reservationId);
    }

    // Calculate the total amount for the stay
    private double calculateTotalAmount(Reservation reservation) {
        // Example calculation: total amount = price per night * number of nights
        double roomPrice = reservation.getRoomPrice();
        long duration = (reservation.getCheckOut().getTime() - reservation.getCheckIn().getTime()) / (1000 * 60 * 60 * 24); // Duration in days
        return roomPrice * duration;
    }
}