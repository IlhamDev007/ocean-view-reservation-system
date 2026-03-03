package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

public class AddReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("addReservation.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String guestName = req.getParameter("guestName");
            String guestAddress = req.getParameter("guestAddress");
            String contactNumber = req.getParameter("contactNumber");
            String roomType = req.getParameter("roomType");

            Date checkIn = Date.valueOf(req.getParameter("checkInDate"));
            Date checkOut = Date.valueOf(req.getParameter("checkOutDate"));

            Reservation r = new Reservation(guestName, guestAddress, contactNumber, roomType, checkIn, checkOut);

            int newId = reservationService.addReservation(r);

            req.setAttribute("success", "✅ Reservation saved! Your Reservation ID is: " + newId);
            RequestDispatcher rd = req.getRequestDispatcher("addReservation.jsp");
            rd.forward(req, resp);

        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", "❌ " + ex.getMessage());
            try {
                req.getRequestDispatcher("addReservation.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect("addReservation.jsp");
            }
        } catch (Exception ex) {
            req.setAttribute("error", "❌ Server error: " + ex.getMessage());
            try {
                req.getRequestDispatcher("addReservation.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect("addReservation.jsp");
            }
        }
    }
}