package com.oceanview.servlet;

import com.oceanview.service.BillingService;
import com.oceanview.service.ReservationService;
import com.oceanview.model.Bill;
import com.oceanview.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    // Handle GET requests - Display the bill for a specific reservation
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("viewBill".equals(action)) {
            viewBill(request, response);
        }
    }

    // Handle POST requests - Generate or update the bill
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("generateBill".equals(action)) {
            generateBill(request, response);
        }
    }

    // Display the bill for a specific reservation
    private void viewBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        BillingService billingService = new BillingService();
        Bill bill = billingService.getBillByReservationId(reservationId);

        if (bill != null) {
            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/WEB-INF/views/billing/bill.jsp").forward(request, response);
        } else {
            response.sendRedirect("billing?error=Bill not found");
        }
    }

    // Generate a bill for a reservation
    private void generateBill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservationId = Integer.parseInt(request.getParameter("reservationId"));
        ReservationService reservationService = new ReservationService();
        Reservation reservation = reservationService.getReservationById(reservationId);

        if (reservation != null) {
            BillingService billingService = new BillingService();
            Bill bill = billingService.generateBill(reservation); // Generate the bill based on reservation data

            // Save the bill to the database (assuming a method in BillingService)
            boolean success = billingService.saveBill(bill);

            if (success) {
                response.sendRedirect("billing?success=Bill generated successfully");
            } else {
                response.sendRedirect("billing?error=Failed to generate bill");
            }
        } else {
            response.sendRedirect("billing?error=Reservation not found");
        }
    }
}