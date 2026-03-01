package com.oceanview.servlet;

import com.oceanview.service.ReservationService;
import com.oceanview.service.RoomService;
import com.oceanview.model.Reservation;
import com.oceanview.model.Room;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/staff")
public class StaffServlet extends HttpServlet {

    // Handle GET requests - Display staff dashboard and manage actions
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("viewRooms".equals(action)) {
            viewRooms(request, response);
        } else if ("viewReservations".equals(action)) {
            viewReservations(request, response);
        } else {
            displayDashboard(request, response);
        }
    }

    // Handle POST requests - Create, Update, or Cancel reservations and check-in/check-out
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("createReservation".equals(action)) {
            createReservation(request, response);
        } else if ("updateReservation".equals(action)) {
            updateReservation(request, response);
        } else if ("cancelReservation".equals(action)) {
            cancelReservation(request, response);
        } else if ("checkIn".equals(action)) {
            checkInGuest(request, response);
        } else if ("checkOut".equals(action)) {
            checkOutGuest(request, response);
        }
    }

    // Display the Staff Dashboard with statistics
    private void displayDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationService reservationService = new ReservationService();
        int todaysCheckIns = reservationService.getTodaysCheckIns();
        int todaysCheckOuts = reservationService.getTodaysCheckOuts();
        int availableRooms = reservationService.getAvailableRooms();
        int pendingReservations = reservationService.getPendingReservations();

        request.setAttribute("todaysCheckIns", todaysCheckIns);
        request.setAttribute("todaysCheckOuts", todaysCheckOuts);
        request.setAttribute("availableRooms", availableRooms);
        request.setAttribute("pendingReservations", pendingReservations);

        request.getRequestDispatcher("/WEB-INF/views/staff/staff-dashboard.jsp").forward(request, response);
    }

    // View all rooms (for staff)
    private void viewRooms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RoomService roomService = new RoomService();
        List<Room> rooms = roomService.getAllRooms(null, null); // Fetch all rooms
        request.setAttribute("rooms", rooms);

        request.getRequestDispatcher("/WEB-INF/views/staff/view-rooms.jsp").forward(request, response);
    }

    // View all reservations (for staff)
    private void viewReservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationService reservationService = new ReservationService();
        List<Reservation> reservations = reservationService.getAllReservations(null, null); // Fetch all reservations
        request.setAttribute("reservations", reservations);

        request.getRequestDispatcher("/WEB-INF/views/staff/view-reservations.jsp").forward(request, response);
    }

    // Create a new reservation
    private void createReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String guestName = request.getParameter("guestName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String checkInDateStr = request.getParameter("checkInDate");
        String checkOutDateStr = request.getParameter("checkOutDate");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date checkInDate = sdf.parse(checkInDateStr);
            Date checkOutDate = sdf.parse(checkOutDateStr);

            // Create a new Reservation object
            Reservation reservation = new Reservation(guestName, address, contactNumber, roomId, checkInDate, checkOutDate);

            ReservationService reservationService = new ReservationService();
            boolean success = reservationService.createReservation(reservation);

            if (success) {
                response.sendRedirect("staff?success=Reservation created successfully.");
            } else {
                response.sendRedirect("staff?error=Failed to create reservation.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staff?error=Invalid input data.");
        }
    }

    // Update an existing reservation
    private void updateReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        String guestName = request.getParameter("guestName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String checkInDateStr = request.getParameter("checkInDate");
        String checkOutDateStr = request.getParameter("checkOutDate");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date checkInDate = sdf.parse(checkInDateStr);
            Date checkOutDate = sdf.parse(checkOutDateStr);

            Reservation reservation = new Reservation(reservationId, guestName, address, contactNumber, roomId, checkInDate, checkOutDate);

            ReservationService reservationService = new ReservationService();
            boolean success = reservationService.updateReservation(reservation);

            if (success) {
                response.sendRedirect("staff?success=Reservation updated successfully.");
            } else {
                response.sendRedirect("staff?error=Failed to update reservation.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("staff?error=Invalid input data.");
        }
    }

    // Cancel a reservation
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        ReservationService reservationService = new ReservationService();

        boolean success = reservationService.cancelReservation(reservationId);

        if (success) {
            response.sendRedirect("staff?success=Reservation canceled successfully.");
        } else {
            response.sendRedirect("staff?error=Failed to cancel reservation.");
        }
    }

    // Handle guest check-in
    private void checkInGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        ReservationService reservationService = new ReservationService();

        boolean success = reservationService.checkInGuest(reservationId);

        if (success) {
            response.sendRedirect("staff?success=Guest checked in successfully.");
        } else {
            response.sendRedirect("staff?error=Failed to check in guest.");
        }
    }

    // Handle guest check-out
    private void checkOutGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        ReservationService reservationService = new ReservationService();

        boolean success = reservationService.checkOutGuest(reservationId);

        if (success) {
            response.sendRedirect("staff?success=Guest checked out successfully.");
        } else {
            response.sendRedirect("staff?error=Failed to check out guest.");
        }
    }
}