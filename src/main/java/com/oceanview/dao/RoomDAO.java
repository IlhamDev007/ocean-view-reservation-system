package com.oceanview.dao;

import com.oceanview.model.Room;
import com.oceanview.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    // Add a new room to the database
    public boolean addRoom(Room room) {
        String query = "INSERT INTO rooms (room_type, price, availability) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, room.getRoomType());
            stmt.setDouble(2, room.getPrice());
            stmt.setBoolean(3, room.isAvailable());

            return stmt.executeUpdate() > 0; // Execute the query and return success status
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update room details in the database
    public boolean updateRoom(int roomId, String roomType, double price) {
        String query = "UPDATE rooms SET room_type = ?, price = ? WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, roomType);
            stmt.setDouble(2, price);
            stmt.setInt(3, roomId);

            return stmt.executeUpdate() > 0; // Execute the update query
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a room from the database
    public boolean deleteRoom(int roomId) {
        String query = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0; // Execute the delete query
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a room by its ID from the database
    public Room getRoomById(int roomId) {
        String query = "SELECT * FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price"),
                        rs.getBoolean("availability")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all rooms from the database, optionally filtered by a specific attribute
    public List<Room> getAllRooms(String filter, String filterValue) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";

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
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price"),
                        rs.getBoolean("availability")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
}