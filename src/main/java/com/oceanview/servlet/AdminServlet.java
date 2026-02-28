package com.oceanview.servlet;

import com.oceanview.service.RoomService;
import com.oceanview.service.ReservationService;
import com.oceanview.service.ReportService;
import com.oceanview.model.Room;
import com.oceanview.model.Reservation;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    // Handle GET requests - Display admin dashboard and manage actions
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("viewReservations".equals(action)) {
            viewReservations(request, response);
        } else if ("addRoom".equals(action)) {
            // Handle adding a new room
            response.sendRedirect("admin/add-room.jsp");
        } else {
            displayDashboard(request, response);
        }
    }

    // Handle POST requests - Add, Update, or Delete rooms and reservations
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("addRoom".equals(action)) {
            addRoom(request, response);
        } else if ("updateRoom".equals(action)) {
            updateRoom(request, response);
        } else if ("deleteRoom".equals(action)) {
            deleteRoom(request, response);
        } else if ("generateReport".equals(action)) {
            generateReport(request, response);
        }
    }

    // Display the Admin Dashboard with statistics
    private void displayDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReportService reportService = new ReportService();
        int totalUsers = reportService.getTotalUsers();
        int totalRooms = reportService.getTotalRooms();
        int totalReservations = reportService.getTotalReservations();
        double monthlyRevenue = reportService.getMonthlyRevenue();

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalRooms", totalRooms);
        request.setAttribute("totalReservations", totalReservations);
        request.setAttribute("monthlyRevenue", monthlyRevenue);

        request.getRequestDispatcher("/WEB-INF/views/admin/admin-dashboard.jsp").forward(request, response);
    }

    // Add a new room to the system
    private void addRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roomType = request.getParameter("roomType");
        double price = Double.parseDouble(request.getParameter("price"));
        RoomService roomService = new RoomService();
        Room room = new Room(roomType, price);
        roomService.addRoom(room);

        response.sendRedirect("admin?success=Room added successfully.");
    }

    // Update room information
    private void updateRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String roomType = request.getParameter("roomType");
        double price = Double.parseDouble(request.getParameter("price"));
        RoomService roomService = new RoomService();
        roomService.updateRoom(roomId, roomType, price);

        response.sendRedirect("admin?success=Room updated successfully.");
    }

    // Delete a room from the system
    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        RoomService roomService = new RoomService();
        roomService.deleteRoom(roomId);

        response.sendRedirect("admin?success=Room deleted successfully.");
    }
