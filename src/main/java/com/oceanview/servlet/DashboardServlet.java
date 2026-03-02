package com.oceanview.servlet;

import com.oceanview.service.ReportService;
import com.oceanview.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    // Handle GET requests - Display admin and staff dashboard statistics
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userRole = (String) request.getSession().getAttribute("role");

        if ("ADMIN".equals(userRole)) {
            displayAdminDashboard(request, response);
        } else if ("STAFF".equals(userRole)) {
            displayStaffDashboard(request, response);
        } else {
            response.sendRedirect("login.jsp"); // Redirect if user role is not valid
        }
    }

    // Display the Admin Dashboard with statistics
    private void displayAdminDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReportService reportService = new ReportService();
        int totalUsers = reportService.getTotalUsers();
        int totalRooms = reportService.getTotalRooms();
        int totalReservations = reportService.getTotalReservations();
        double monthlyRevenue = reportService.getMonthlyRevenue();

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalRooms", totalRooms);
        request.setAttribute("totalReservations", totalReservations);
        request.setAttribute("monthlyRevenue", monthlyRevenue);

        // Forward to the admin dashboard view
        request.getRequestDispatcher("/WEB-INF/views/admin/admin-dashboard.jsp").forward(request, response);
    }

    // Display the Staff Dashboard with statistics
    private void displayStaffDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationService reservationService = new ReservationService();
        int todaysCheckIns = reservationService.getTodaysCheckIns();
        int todaysCheckOuts = reservationService.getTodaysCheckOuts();
        int availableRooms = reservationService.getAvailableRooms();
        int pendingReservations = reservationService.getPendingReservations();

        request.setAttribute("todaysCheckIns", todaysCheckIns);
        request.setAttribute("todaysCheckOuts", todaysCheckOuts);
        request.setAttribute("availableRooms", availableRooms);
        request.setAttribute("pendingReservations", pendingReservations);

        // Forward to the staff dashboard view
        request.getRequestDispatcher("/WEB-INF/views/staff/staff-dashboard.jsp").forward(request, response);
    }
}