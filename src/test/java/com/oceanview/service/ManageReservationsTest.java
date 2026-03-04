package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ManageReservationsTest {

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

    // Helper: create a reservation for testing, return generated ID
    private int createTestReservation() throws Exception {
        ReservationDAO dao = new ReservationDAO();
        Reservation r = new Reservation(
                "Manage Test Guest",
                "Galle",
                "0771234567",
                "Deluxe",
                Date.valueOf("2026-03-10"),
                Date.valueOf("2026-03-12")
        );
        int id = dao.insert(r);
        assertTrue("Setup failed: could not insert test reservation", id > 0);
        return id;
    }

    /**
     * Test Case: Manage Reservations - List All (empty search)
     * Expected Result: List is returned (NOT NULL).
     * Note: If DB has no data, size can be 0; so we check list != null.
     */
    @Test
    public void testManageListAllReservations() throws Exception {
        ReservationService service = new ReservationService();

        List<Reservation> list = service.search(""); // empty = return all
        boolean result = (list != null);

        assertTrue("Expected a non-null list for list-all", result);
        originalOut.println("Manage Reservations List All: " + result + " | Count: " + list.size());
    }

    /**
     * Test Case: Manage Reservations - Search by Reservation ID
     * Expected Result: List size = 1 and returned reservation matches inserted ID.
     */
    @Test
    public void testManageSearchById() throws Exception {
        ReservationService service = new ReservationService();
        ReservationDAO dao = new ReservationDAO();

        int id = createTestReservation();

        List<Reservation> resultList = service.search(String.valueOf(id)); // numeric => findById first
        boolean result = (resultList != null && resultList.size() == 1 && resultList.get(0).getReservationId() == id);

        assertTrue("Expected search by ID to return exactly one matching reservation", result);
        originalOut.println("Manage Reservations Search By ID: " + result + " | ID: " + id);

        // cleanup
        dao.deleteById(id);
    }

    /**
     * Test Case: Manage Reservations - Update Reservation
     * Expected Result: updateReservation() returns TRUE and DB shows updated values.
     */
    @Test
    public void testManageUpdateReservation() throws Exception {
        ReservationService service = new ReservationService();
        ReservationDAO dao = new ReservationDAO();

        int id = createTestReservation();

        // Update guest name + room type
        Reservation update = new Reservation();
        update.setReservationId(id);
        update.setGuestName("Updated Guest");
        update.setGuestAddress("Updated Address");
        update.setContactNumber("0771234567"); // still valid
        update.setRoomType("Suite");
        update.setCheckInDate(Date.valueOf("2026-03-10"));
        update.setCheckOutDate(Date.valueOf("2026-03-12"));

        boolean ok = service.updateReservation(update);

        // Verify in DB
        Reservation after = dao.findById(id);

        boolean result = ok
                && after != null
                && "Updated Guest".equals(after.getGuestName())
                && "Suite".equals(after.getRoomType());

        assertTrue("Expected update to succeed and reflect in database", result);
        originalOut.println("Manage Reservations Update: " + result + " | ID: " + id);

        // cleanup
        dao.deleteById(id);
    }

    /**
     * Test Case: Manage Reservations - Delete Reservation
     * Expected Result: deleteReservationById() returns TRUE and record no longer exists.
     */
    @Test
    public void testManageDeleteReservation() throws Exception {
        ReservationService service = new ReservationService();
        ReservationDAO dao = new ReservationDAO();

        int id = createTestReservation();

        boolean ok = service.deleteReservationById(String.valueOf(id));
        Reservation after = dao.findById(id);

        boolean result = ok && (after == null);

        assertTrue("Expected delete to succeed and reservation to be removed", result);
        originalOut.println("Manage Reservations Delete: " + result + " | ID: " + id);
    }
}