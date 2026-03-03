package com.oceanview.service;

import com.oceanview.model.Reservation;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BillingService {

    public int calculateNights(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and Check-out dates are required");
        }

        LocalDate in = checkIn.toLocalDate();
        LocalDate out = checkOut.toLocalDate();

        long nights = ChronoUnit.DAYS.between(in, out);

        if (nights <= 0) {
            throw new IllegalArgumentException("Invalid date range. Check-out must be after check-in.");
        }

        return (int) nights;
    }

    public double getRoomRatePerNight(String roomType) {
        if (roomType == null) return 0;

        switch (roomType.trim().toLowerCase()) {
            case "single":
                return 5000.00;
            case "double":
                return 8000.00;
            case "deluxe":
                return 12000.00;
            default:
                return 6000.00; // default rate for unknown type
        }
    }

    public double calculateTotal(Reservation r) {
        int nights = calculateNights(r.getCheckInDate(), r.getCheckOutDate());
        double rate = getRoomRatePerNight(r.getRoomType());
        return nights * rate;
    }
}