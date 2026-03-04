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

public class ViewReservationTest {

    private PrintStream originalOut;

    @Before
    public void muteSystemOut() {
        originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream())); // hide DBConnection prints
    }

    @After
    public void restoreSystemOut() {
        System.setOut(originalOut);
    }

    /**
     * Test Case: View Reservation - Valid Existing ID
     * Expected Result: Reservation object is returned (NOT NULL)
     * NOTE: Uses DB (inserts a test record, views it, then deletes it)
     */
    @Test
    public void testViewReservationValidId() throws Exception {
        ReservationService service = new ReservationService();
        ReservationDAO dao = new ReservationDAO();

        // 1) Insert a reservation to guarantee an existing ID
        Reservation r = new Reservation(
                "View Test Guest",
                "Galle",
                "0771234567",
                "Deluxe",
                Date.valueOf("2026-03-10"),
                Date.valueOf("2026-03-12")
        );

        int newId = dao.insert(r);
        assertTrue("Setup failed: could not insert test reservation", newId > 0);

        // 2) View / Display details
        Reservation found = service.getReservationByIdOrNull(String.valueOf(newId));
        boolean result = (found != null);

        assertTrue("Expected reservation details for valid existing ID", result);
        assertEquals("View Test Guest", found.getGuestName());

        originalOut.println("View Reservation Valid ID: " + result + " | Found ID: " + newId);

        // 3) Cleanup
        dao.deleteById(newId);
    }

    /**
     * Test Case: View Reservation - Non Existing ID
     * Expected Result: NULL is returned
     */
    @Test
    public void testViewReservationNonExistingId() throws Exception {
        ReservationService service = new ReservationService();

        // Pick a very large ID that likely doesn't exist
        Reservation found = service.getReservationByIdOrNull("999999");
        boolean result = (found == null);

        assertTrue("Expected null for non-existing reservation ID", result);
        originalOut.println("View Reservation Non-Existing ID: " + result);
    }

    /**
     * Test Case: View Reservation - Empty ID Input
     * Expected Result: NULL is returned
     */
    @Test
    public void testViewReservationEmptyId() throws Exception {
        ReservationService service = new ReservationService();

        Reservation found = service.getReservationByIdOrNull("   ");
        boolean result = (found == null);

        assertTrue("Expected null for empty reservation ID input", result);
        originalOut.println("View Reservation Empty ID: " + result);
    }

    /**
     * Test Case: View Reservation - Non Numeric ID Input
     * Expected Result: NULL is returned
     */
    @Test
    public void testViewReservationNonNumericId() throws Exception {
        ReservationService service = new ReservationService();

        Reservation found = service.getReservationByIdOrNull("ABC123");
        boolean result = (found == null);

        assertTrue("Expected null for non-numeric reservation ID input", result);
        originalOut.println("View Reservation Non-Numeric ID: " + result);
    }
}