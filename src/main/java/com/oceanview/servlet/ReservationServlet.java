package com.oceanview.servlet;

import com.oceanview.service.ReservationService;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/reservations")
public class ReservationServlet extends HttpServlet {

    // Handle GET requests for displaying reservation details or reservation list
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("view".equals(action)) {
            viewReservation(request, response);
        } else {
            listReservations(request, response);
        }
    }

    // Handle POST requests for creating or updating reservations
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createReservation(request, response);
        } else if ("update".equals(action)) {
            updateReservation(request, response);
        } else if ("cancel".equals(action)) {
            cancelReservation(request, response);
        }
    }

    // Display reservation details
    private void viewReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        ReservationService reservationService = new ReservationService();
        Reservation reservation = reservationService.getReservationById(reservationId);

        if (reservation != null) {
            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/views/reservation/reservation-details.jsp").forward(request, response);
        } else {
            response.sendRedirect("reservations?error=Reservation not found");
        }
    }

    // List all reservations for admin view with filtering options (if needed)
    private void listReservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationService reservationService = new ReservationService();
        String filter = request.getParameter("filter");
        String filterValue = request.getParameter("filterValue");

        // For simplicity, we assume no filtering is applied here
        // You can implement advanced filtering based on room type, dates, etc.
        request.setAttribute("reservations", reservationService.getAllReservations(filter, filterValue));
        request.getRequestDispatcher("/WEB-INF/views/admin/all-reservations.jsp").forward(request, response);
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
                response.sendRedirect("reservations?success=Reservation created successfully");
            } else {
                response.sendRedirect("reservations?error=Failed to create reservation");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reservations?error=Invalid input data");
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
                response.sendRedirect("reservations?success=Reservation updated successfully");
            } else {
                response.sendRedirect("reservations?error=Failed to update reservation");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reservations?error=Invalid input data");
        }
    }

    // Cancel a reservation
    private void cancelReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        ReservationService reservationService = new ReservationService();

        boolean success = reservationService.cancelReservation(reservationId);

        if (success) {
            response.sendRedirect("reservations?success=Reservation canceled successfully");
        } else {
            response.sendRedirect("reservations?error=Failed to cancel reservation");
        }
    }
}