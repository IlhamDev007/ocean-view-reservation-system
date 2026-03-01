package com.oceanview.service;

import com.oceanview.dao.RoomDAO;
import com.oceanview.model.Room;
import java.util.List;

public class RoomService {

    private final RoomDAO roomDAO;

    public RoomService() {
        this.roomDAO = new RoomDAO();
    }

    // Add a new room to the database
    public boolean addRoom(Room room) {
        if (isValidRoom(room)) {
            return roomDAO.addRoom(room); // Call DAO to insert the room into the database
        }
        return false; // Invalid room data
    }

    // Update room details
    public boolean updateRoom(int roomId, String roomType, double price) {
        if (isValidRoomType(roomType) && price > 0) {
            return roomDAO.updateRoom(roomId, roomType, price); // Call DAO to update the room details
        }
        return false; // Invalid room data
    }

    // Delete a room from the database
    public boolean deleteRoom(int roomId) {
        return roomDAO.deleteRoom(roomId); // Call DAO to delete the room
    }

    // Get a specific room by its ID
    public Room getRoomById(int roomId) {
        return roomDAO.getRoomById(roomId); // Call DAO to get the room details
    }

    // Get all rooms available (filtered if necessary)
    public List<Room> getAllRooms(String filter, String filterValue) {
        return roomDAO.getAllRooms(filter, filterValue); // Call DAO to fetch all rooms with filters
    }

    // Validate room data (business logic)
    private boolean isValidRoom(Room room) {
        return room != null && isValidRoomType(room.getRoomType()) && room.getPrice() > 0;
    }

    // Validate room type (example business rule)
    private boolean isValidRoomType(String roomType) {
        return roomType != null && !roomType.isEmpty();
    }
}