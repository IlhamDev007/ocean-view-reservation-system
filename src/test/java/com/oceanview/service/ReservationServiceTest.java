package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Date;

import static org.junit.Assert.*;

public class ReservationServiceTest {

    private PrintStream originalOut;

    @Before
    public void muteSystemOut() {
        // Save original System.out
        originalOut = System.out;

        // Redirect System.out to nowhere (silence)
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @After
    public void restoreSystemOut() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    /**
     * Test Case: Add Reservation - Success (Valid Inputs)
     * Expected Result: A new reservation ID (> 0) is returned.
     * NOTE: Requires database connection and reservations table.
     */
    @Test
    public void testAddReservationSuccess() throws Exception {
        ReservationService service = new ReservationService();
        ReservationDAO dao = new ReservationDAO();

        Reservation r = new Reservation(
                "Test Guest",
                "No 10, Galle",
                "0771234567",
                "Deluxe",
                Date.valueOf("2026-03-10"),
                Date.valueOf("2026-03-12")
        );

        int newId = service.addReservation(r);
        boolean result = (newId > 0);

        assertTrue("Expected a generated reservation ID > 0", result);

        // ✅ show only your clean output (use originalOut)
        originalOut.println("Add Reservation Success: " + result + " | New ID: " + newId);

        // Optional verification
        Reservation saved = dao.findById(newId);
        assertNotNull("Expected reservation to be found in DB after insert", saved);
        assertEquals("Test Guest", saved.getGuestName());

        // Cleanup
        dao.deleteById(newId);
    }

    /**
     * Test Case: Empty Guest Name
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testAddReservationEmptyGuestName() {
        ReservationService service = new ReservationService();

        Reservation r = new Reservation(
                "   ",
                "No 10, Galle",
                "0771234567",
                "Deluxe",
                Date.valueOf("2026-03-10"),
                Date.valueOf("2026-03-12")
        );

        boolean result;
        try {
            service.addReservation(r);
            result = false;
        } catch (IllegalArgumentException ex) {
            result = true;
        } catch (Exception ex) {
            result = false;
        }

        assertTrue("Expected validation failure for empty guest name", result);
        originalOut.println("Add Reservation Empty Guest Name: " + result);
    }

    /**
     * Test Case: Invalid Contact Number
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testAddReservationInvalidContact() {
        ReservationService service = new ReservationService();

        Reservation r = new Reservation(
                "Test Guest",
                "No 10, Galle",
                "07123",
                "Deluxe",
                Date.valueOf("2026-03-10"),
                Date.valueOf("2026-03-12")
        );

        boolean result;
        try {
            service.addReservation(r);
            result = false;
        } catch (IllegalArgumentException ex) {
            result = true;
        } catch (Exception ex) {
            result = false;
        }

        assertTrue("Expected validation failure for invalid contact number", result);
        originalOut.println("Add Reservation Invalid Contact: " + result);
    }

    /**
     * Test Case: Invalid Dates (check-out must be after check-in)
     * Expected Result: IllegalArgumentException is thrown.
     */
    @Test
    public void testAddReservationInvalidDates() {
        ReservationService service = new ReservationService();

        Reservation r = new Reservation(
                "Test Guest",
                "No 10, Galle",
                "0771234567",
                "Deluxe",
                Date.valueOf("2026-03-12"),
                Date.valueOf("2026-03-12")
        );

        boolean result;
        try {
            service.addReservation(r);
            result = false;
        } catch (IllegalArgumentException ex) {
            result = true;
        } catch (Exception ex) {
            result = false;
        }

        assertTrue("Expected validation failure for invalid date range", result);
        originalOut.println("Add Reservation Invalid Dates: " + result);
    }
}