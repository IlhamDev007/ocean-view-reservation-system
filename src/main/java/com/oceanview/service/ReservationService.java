package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.Reservation;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    // =========================
    // ADD RESERVATION
    // =========================
    public int addReservation(Reservation r) throws Exception {
        validate(r, false);
        return reservationDAO.insert(r);
    }

    // =========================
    // LIST ALL (used by View page + Manage page)
    // =========================
    public List<Reservation> listAllReservations() throws Exception {
        return reservationDAO.findAll();
    }

    // =========================
    // SEARCH (used in Manage Reservations)
    // - if keyword empty -> return all
    // - if keyword is number -> try findById first
    // =========================
    public List<Reservation> search(String keyword) throws Exception {
        if (isBlank(keyword)) {
            return reservationDAO.findAll();
        }

        String k = keyword.trim();

        // If user typed numeric ID, try that first
        try {
            int id = Integer.parseInt(k);
            if (id > 0) {
                Reservation r = reservationDAO.findById(id);
                if (r != null) return Arrays.asList(r);
            }
        } catch (NumberFormatException ignored) {
            // Not a number -> continue normal search
        }

        return reservationDAO.search(k);
    }

    // =========================
    // GET BY ID (returns null if not found)
    // =========================
    public Reservation getReservationById(String idText) throws Exception {
        int id = parseId(idText);
        return reservationDAO.findById(id);
    }

    // =========================
    // GET BY ID OR NULL (safe for View page)
    // - returns null for invalid input OR not found
    // =========================
    public Reservation getReservationByIdOrNull(String idText) throws Exception {
        if (isBlank(idText)) return null;

        try {
            int id = Integer.parseInt(idText.trim());
            if (id <= 0) return null;
            return reservationDAO.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // =========================
    // GET BY ID OR THROW (used for Bill and Edit)
    // =========================
    public Reservation getReservationByIdOrThrow(String idText) throws Exception {
        int id = parseId(idText);
        Reservation r = reservationDAO.findById(id);
        if (r == null) throw new IllegalArgumentException("No reservation found for ID: " + id);
        return r;
    }

    // =========================
    // UPDATE RESERVATION
    // =========================
    public boolean updateReservation(Reservation r) throws Exception {
        validate(r, true);
        return reservationDAO.update(r);
    }

    // =========================
    // DELETE RESERVATION
    // =========================
    public boolean deleteReservationById(String idText) throws Exception {
        int id = parseId(idText);
        return reservationDAO.deleteById(id);
    }

    // =========================
    // HELPERS
    // =========================
    private int parseId(String idText) {
        if (isBlank(idText)) throw new IllegalArgumentException("Reservation ID is required");

        try {
            int id = Integer.parseInt(idText.trim());
            if (id <= 0) throw new IllegalArgumentException("Reservation ID must be positive");
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Reservation ID must be a number");
        }
    }

    // isUpdate = true means reservationId must exist
    private void validate(Reservation r, boolean isUpdate) {
        if (r == null) throw new IllegalArgumentException("Reservation is null");

        if (isUpdate && r.getReservationId() <= 0) {
            throw new IllegalArgumentException("Reservation ID is missing");
        }

        if (isBlank(r.getGuestName())) throw new IllegalArgumentException("Guest name is required");
        if (isBlank(r.getGuestAddress())) throw new IllegalArgumentException("Guest address is required");
        if (isBlank(r.getContactNumber())) throw new IllegalArgumentException("Contact number is required");
        if (isBlank(r.getRoomType())) throw new IllegalArgumentException("Room type is required");

        String contact = r.getContactNumber().trim();
        if (!contact.matches("\\d{7,15}")) {
            throw new IllegalArgumentException("Contact number must be 7 to 15 digits");
        }

        Date in = r.getCheckInDate();
        Date out = r.getCheckOutDate();

        if (in == null) throw new IllegalArgumentException("Check-in date is required");
        if (out == null) throw new IllegalArgumentException("Check-out date is required");

        if (!out.after(in)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}