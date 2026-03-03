package com.oceanview.controller;

import com.oceanview.model.DashboardMetrics;
import com.oceanview.service.DashboardService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DashboardServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            DashboardMetrics m = dashboardService.getMetrics();

            req.setAttribute("metrics", m);
            req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ Dashboard error: " + e.getMessage());
            try {
                req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
            } catch (Exception ex) {
                resp.sendRedirect(req.getContextPath() + "/login.jsp");
            }
        }
    }
}