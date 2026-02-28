package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // Create a new reservation in the database
    public boolean createReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (guest_name, guest_address, guest_contact_number, room_id, check_in, check_out, status, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, reservation.getGuestName());
            stmt.setString(2, reservation.getGuestAddress());
            stmt.setString(3, reservation.getGuestContactNumber());
            stmt.setInt(4, reservation.getRoomId());
            stmt.setDate(5, new java.sql.Date(reservation.getCheckIn().getTime()));
            stmt.setDate(6, new java.sql.Date(reservation.getCheckOut().getTime()));
            stmt.setString(7, reservation.getStatus().toString());
            stmt.setDouble(8, reservation.getTotalAmount());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing reservation
    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE reservations SET guest_name = ?, guest_address = ?, guest_contact_number = ?, room_id = ?, check_in = ?, check_out = ?, status = ?, total_amount = ? WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, reservation.getGuestName());
            stmt.setString(2, reservation.getGuestAddress());
            stmt.setString(3, reservation.getGuestContactNumber());
            stmt.setInt(4, reservation.getRoomId());
            stmt.setDate(5, new java.sql.Date(reservation.getCheckIn().getTime()));
            stmt.setDate(6, new java.sql.Date(reservation.getCheckOut().getTime()));
            stmt.setString(7, reservation.getStatus().toString());
            stmt.setDouble(8, reservation.getTotalAmount());
            stmt.setInt(9, reservation.getReservationId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cancel a reservation
    public boolean cancelReservation(int reservationId) {
        String query = "UPDATE reservations SET status = 'CANCELLED' WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a reservation by ID
    public Reservation getReservationById(int reservationId) {
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setGuestName(rs.getString("guest_name"));
                reservation.setGuestAddress(rs.getString("guest_address"));
                reservation.setGuestContactNumber(rs.getString("guest_contact_number"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setCheckIn(rs.getDate("check_in"));
                reservation.setCheckOut(rs.getDate("check_out"));
                reservation.setStatus(rs.getString("status"));
                reservation.setTotalAmount(rs.getDouble("total_amount"));
                return reservation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all reservations with optional filtering
    public List<Reservation> getAllReservations(String filter, String filterValue) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";

        if (filter != null && filterValue != null) {
            query += " WHERE " + filter + " LIKE ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (filter != null && filterValue != null) {
                stmt.setString(1, "%" + filterValue + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setGuestName(rs.getString("guest_name"));
                reservation.setGuestAddress(rs.getString("guest_address"));
                reservation.setGuestContactNumber(rs.getString("guest_contact_number"));
                reservation.setRoomId(rs.getInt("room_id"));
                reservation.setCheckIn(rs.getDate("check_in"));
                reservation.setCheckOut(rs.getDate("check_out"));
                reservation.setStatus(rs.getString("status"));
                reservation.setTotalAmount(rs.getDouble("total_amount"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}