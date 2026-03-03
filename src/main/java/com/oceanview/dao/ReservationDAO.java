package com.oceanview.dao;

import com.oceanview.model.Reservation;
import com.oceanview.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // =========================
    // DASHBOARD: COUNT ALL
    // =========================
    public int countAll() throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            return rs.getInt(1);
        }
    }

    // =========================
    // INSERT (Add Reservation)
    // =========================
    public int insert(Reservation r) throws Exception {
        String sql = "INSERT INTO reservations " +
                "(guest_name, guest_address, contact_number, room_type, check_in_date, check_out_date) " +
                "VALUES (?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getGuestAddress());
            ps.setString(3, r.getContactNumber());
            ps.setString(4, r.getRoomType());
            ps.setDate(5, r.getCheckInDate());
            ps.setDate(6, r.getCheckOutDate());

            int rows = ps.executeUpdate();
            if (rows == 0) return -1;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        }
    }

    // =========================
    // FIND BY ID (View details)
    // =========================
    public Reservation findById(int id) throws Exception {
        String sql = "SELECT reservation_id, guest_name, guest_address, contact_number, room_type, " +
                "check_in_date, check_out_date " +
                "FROM reservations WHERE reservation_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
                return null;
            }
        }
    }

    // =========================
    // FIND ALL (View all / Manage)
    // =========================
    public List<Reservation> findAll() throws Exception {
        String sql = "SELECT reservation_id, guest_name, guest_address, contact_number, room_type, " +
                "check_in_date, check_out_date " +
                "FROM reservations ORDER BY reservation_id DESC";

        List<Reservation> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

        return list;
    }

    // =========================
    // SEARCH (keyword search across columns)
    // =========================
    public List<Reservation> search(String keyword) throws Exception {
        String like = "%" + keyword + "%";

        String sql = "SELECT reservation_id, guest_name, guest_address, contact_number, room_type, " +
                "check_in_date, check_out_date " +
                "FROM reservations " +
                "WHERE guest_name LIKE ? OR guest_address LIKE ? OR contact_number LIKE ? OR room_type LIKE ? " +
                "ORDER BY reservation_id DESC";

        List<Reservation> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }

        return list;
    }

    // =========================
    // UPDATE (Edit reservation)
    // =========================
    public boolean update(Reservation r) throws Exception {
        String sql = "UPDATE reservations SET guest_name=?, guest_address=?, contact_number=?, room_type=?, " +
                "check_in_date=?, check_out_date=? WHERE reservation_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getGuestName());
            ps.setString(2, r.getGuestAddress());
            ps.setString(3, r.getContactNumber());
            ps.setString(4, r.getRoomType());
            ps.setDate(5, r.getCheckInDate());
            ps.setDate(6, r.getCheckOutDate());
            ps.setInt(7, r.getReservationId());

            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // DELETE
    // =========================
    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM reservations WHERE reservation_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // Row mapper (ResultSet -> Reservation)
    // =========================
    private Reservation mapRow(ResultSet rs) throws Exception {
        Reservation r = new Reservation();
        r.setReservationId(rs.getInt("reservation_id"));
        r.setGuestName(rs.getString("guest_name"));
        r.setGuestAddress(rs.getString("guest_address"));
        r.setContactNumber(rs.getString("contact_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setCheckInDate(rs.getDate("check_in_date"));
        r.setCheckOutDate(rs.getDate("check_out_date"));
        return r;
    }
}