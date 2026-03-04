package com.oceanview.service;

import com.oceanview.model.Reservation;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

public class BillingServiceTest {

    private static final double DELTA = 0.001;

    /**
     * Test Case 1: Calculate Nights (Valid Dates)
     * Expected: Correct nights count is returned.
     * Example: 2026-03-10 to 2026-03-13 => 3 nights
     */
    @Test
    public void testCalculateNightsValid() {
        BillingService billing = new BillingService();

        int nights = billing.calculateNights(
                Date.valueOf("2026-03-10"),
                Date.valueOf("2026-03-13")
        );

        boolean result = (nights == 3);
        assertTrue("Expected nights = 3", result);
        System.out.println("Calculate Nights (Valid): " + result + " | Nights=" + nights);
    }

    /**
     * Test Case 2: Room Rate Per Night (Deluxe)
     * Expected: Deluxe rate should be 5000.00 according to BillingService rules.
     */
    @Test
    public void testRoomRateDeluxe() {
        BillingService billing = new BillingService();

        double rate = billing.getRoomRatePerNight("Deluxe");
        boolean result = Math.abs(rate - 5000.00) < DELTA;

        assertTrue("Expected Deluxe rate = 5000.00", result);
        System.out.println("Room Rate (Deluxe): " + result + " | Rate=" + rate);
    }

    /**
     * Test Case 3: Calculate Total Bill (Reservation)
     * Expected: total = nights * rate
     * Example: Double = 1800.00, 2 nights => 3600.00
     */
    @Test
    public void testCalculateTotalBill() {
        BillingService billing = new BillingService();

        Reservation r = new Reservation(
                "Test Guest",
                "Galle",
                "0771234567",
                "Double", // 1800.00
                Date.valueOf("2026-03-10"),
                Date.valueOf("2026-03-12") // 2 nights
        );

        double total = billing.calculateTotal(r);
        boolean result = Math.abs(total - 3600.00) < DELTA;

        assertTrue("Expected total = 3600.00", result);
        System.out.println("Calculate Total Bill: " + result + " | Total=" + total);
    }

    /**
     * Test Case 4: Print Bill Summary (Simulation)
     * We simulate "Print Bill" by generating and displaying bill details.
     * Expected: Bill details print without error and total is valid (>0).
     */
    @Test
    public void testPrintBillSummary() {
        BillingService billing = new BillingService();

        Reservation r = new Reservation(
                "Print Guest",
                "Galle",
                "0771234567",
                "Single", // 1000.00
                Date.valueOf("2026-03-01"),
                Date.valueOf("2026-03-03") // 2 nights => 2000.00
        );

        int nights = billing.calculateNights(r.getCheckInDate(), r.getCheckOutDate());
        double rate = billing.getRoomRatePerNight(r.getRoomType());
        double total = billing.calculateTotal(r);

        boolean result = (nights > 0) && (rate > 0) && (total > 0);

        // Print bill (console output)
        System.out.println("----- Ocean View Bill -----");
        System.out.println("Guest Name   : " + r.getGuestName());
        System.out.println("Room Type    : " + r.getRoomType());
        System.out.println("Check-In     : " + r.getCheckInDate());
        System.out.println("Check-Out    : " + r.getCheckOutDate());
        System.out.println("Nights       : " + nights);
        System.out.println("Rate/Night   : " + rate);
        System.out.println("Total Amount : " + total);
        System.out.println("---------------------------");

        assertTrue("Expected bill summary values to be valid", result);
        System.out.println("Print Bill Summary: " + result);
    }
}