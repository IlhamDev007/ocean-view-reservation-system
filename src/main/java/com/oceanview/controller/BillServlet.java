package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.BillingService;
import com.oceanview.service.ReservationService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BillServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final BillingService billingService = new BillingService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.getRequestDispatcher("bill.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendRedirect("dashboard.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String idText = req.getParameter("reservationId");

            Reservation r = reservationService.getReservationByIdOrThrow(idText);

            int nights = billingService.calculateNights(r.getCheckInDate(), r.getCheckOutDate());
            double rate = billingService.getRoomRatePerNight(r.getRoomType());
            double total = nights * rate;

            req.setAttribute("reservation", r);
            req.setAttribute("nights", nights);
            req.setAttribute("rate", rate);
            req.setAttribute("total", total);

            req.getRequestDispatcher("bill.jsp").forward(req, resp);

        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", "❌ " + ex.getMessage());
            try {
                req.getRequestDispatcher("bill.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect("bill.jsp");
            }
        } catch (Exception ex) {
            req.setAttribute("error", "❌ Server error: " + ex.getMessage());
            try {
                req.getRequestDispatcher("bill.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect("bill.jsp");
            }
        }
    }
}