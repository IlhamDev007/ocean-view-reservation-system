package com.oceanview.service;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.model.DashboardMetrics;
import com.oceanview.model.Reservation;

import java.util.List;

public class DashboardService {

    // ✅ Default total rooms (change if you want)
    private static final int TOTAL_ROOMS = 50;

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final BillingService billingService = new BillingService();

    public DashboardMetrics getMetrics() throws Exception {
        DashboardMetrics m = new DashboardMetrics();

        int totalReservations = reservationDAO.countAll();
        int availableRooms = TOTAL_ROOMS - totalReservations;
        if (availableRooms < 0) availableRooms = 0;

        // Revenue: sum of each reservation total (nights × rate)
        double revenue = 0.0;
        List<Reservation> all = reservationDAO.findAll();
        for (Reservation r : all) {
            revenue += billingService.calculateTotal(r);
        }

        m.setTotalRooms(TOTAL_ROOMS);
        m.setTotalReservations(totalReservations);
        m.setAvailableRooms(availableRooms);
        m.setTotalRevenue(revenue);

        return m;
    }
}