package com.oceanview.service;

import com.oceanview.dao.ReportDAO;

public class ReportService {

    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    // Get total number of users (staff + admin)
    public int getTotalUsers() {
        return reportDAO.getTotalUsers();
    }

    // Get total number of rooms
    public int getTotalRooms() {
        return reportDAO.getTotalRooms();
    }

    // Get total number of reservations
    public int getTotalReservations() {
        return reportDAO.getTotalReservations();
    }

    // Get total revenue for the current month
    public double getMonthlyRevenue() {
        return reportDAO.getMonthlyRevenue();
    }
}