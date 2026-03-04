package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ViewReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // 1) Always load all reservations for table
            List<Reservation> list = reservationService.listAllReservations();
            req.setAttribute("list", list);

            // 2) If URL has id= (click view button), show that reservation details
            String idText = req.getParameter("id");
            if (idText != null && !idText.trim().isEmpty()) {
                Reservation r = reservationService.getReservationByIdOrNull(idText);
                if (r != null) {
                    req.setAttribute("reservation", r);
                } else {
                    req.setAttribute("error", "❌ No reservation found for ID: " + idText);
                }
            }

            req.getRequestDispatcher("/viewReservation.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Search by reservation ID from form
            String idText = req.getParameter("reservationId");

            // Load list for table
            List<Reservation> list = reservationService.listAllReservations();
            req.setAttribute("list", list);

            if (idText == null || idText.trim().isEmpty()) {
                req.setAttribute("error", "❌ Reservation ID is required");
            } else {
                Reservation r = reservationService.getReservationByIdOrNull(idText);
                if (r != null) {
                    req.setAttribute("reservation", r);
                } else {
                    req.setAttribute("error", "❌ No reservation found for ID: " + idText);
                }
            }

            req.getRequestDispatcher("/viewReservation.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Server error: " + e.getMessage());
            try {
                req.getRequestDispatcher("/viewReservation.jsp").forward(req, resp);
            } catch (Exception ex) {
                resp.sendRedirect(req.getContextPath() + "/viewReservation");
            }
        }
    }
}