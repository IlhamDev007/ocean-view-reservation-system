package com.oceanview.controller;

import com.oceanview.model.Reservation;
import com.oceanview.service.ReservationService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class ManageReservationsServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    // =========================
    // GET: list/search OR open edit form
    // =========================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = normalize(req.getPathInfo()); // "", "/", "/edit"

        // Debug line (optional): check Tomcat console
        System.out.println("[ManageReservationsServlet] GET pathInfo=" + req.getPathInfo() +
                " action=" + action + " id=" + req.getParameter("id") + " q=" + req.getParameter("q"));

        try {
            if (action.equals("") || action.equals("/")) {
                // LIST / SEARCH
                handleList(req, resp);
                return;
            }

            if (action.equals("/edit")) {
                // OPEN EDIT FORM
                handleEdit(req, resp);
                return;
            }

            // Unknown GET path -> go back
            resp.sendRedirect(req.getContextPath() + "/manageReservations");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "❌ " + e.getMessage());
            safeForward(req, resp, "/manageReservations.jsp");
        }
    }

    // =========================
    // POST: update OR delete
    // =========================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = normalize(req.getPathInfo()); // "/update", "/delete"

        // Debug line (optional)
        System.out.println("[ManageReservationsServlet] POST pathInfo=" + req.getPathInfo() + " action=" + action);

        try {
            if (action.equals("/update")) {
                handleUpdate(req, resp);
                return;
            }

            if (action.equals("/delete")) {
                handleDelete(req, resp);
                return;
            }

            // Unknown POST path -> go back
            resp.sendRedirect(req.getContextPath() + "/manageReservations");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/manageReservations?msg=error");
        }
    }

    // =========================
    // Handlers
    // =========================

    private void handleList(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String q = req.getParameter("q"); // search keyword
        List<Reservation> list = reservationService.search(q);

        req.setAttribute("list", list);
        req.setAttribute("q", q);

        // ✅ IMPORTANT: absolute JSP path
        safeForward(req, resp, "/manageReservations.jsp");
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idText = req.getParameter("id");
        Reservation r = reservationService.getReservationByIdOrThrow(idText);

        req.setAttribute("reservation", r);

        // ✅ IMPORTANT: absolute JSP path (fixes edit not opening)
        safeForward(req, resp, "/editReservation.jsp");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = Integer.parseInt(req.getParameter("reservationId"));

        Reservation r = new Reservation();
        r.setReservationId(id);
        r.setGuestName(req.getParameter("guestName"));
        r.setGuestAddress(req.getParameter("guestAddress"));
        r.setContactNumber(req.getParameter("contactNumber"));
        r.setRoomType(req.getParameter("roomType"));

        // Date.valueOf requires "yyyy-mm-dd" format (HTML date input gives that)
        r.setCheckInDate(Date.valueOf(req.getParameter("checkInDate")));
        r.setCheckOutDate(Date.valueOf(req.getParameter("checkOutDate")));

        boolean ok = reservationService.updateReservation(r);

        resp.sendRedirect(req.getContextPath() + "/manageReservations?msg=" + (ok ? "updated" : "notupdated"));
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String idText = req.getParameter("id");
        boolean ok = reservationService.deleteReservationById(idText);

        resp.sendRedirect(req.getContextPath() + "/manageReservations?msg=" + (ok ? "deleted" : "notdeleted"));
    }

    // =========================
    // Helpers
    // =========================

    // Normalize pathInfo:
    // null -> ""
    // "/edit/" -> "/edit"
    private String normalize(String pathInfo) {
        if (pathInfo == null) return "";
        if (pathInfo.length() > 1 && pathInfo.endsWith("/")) {
            return pathInfo.substring(0, pathInfo.length() - 1);
        }
        return pathInfo;
    }

    // Forward to JSP safely (ABSOLUTE PATH REQUIRED)
    private void safeForward(HttpServletRequest req, HttpServletResponse resp, String jsp) throws IOException {
        try {
            // Ensure it starts with "/"
            String path = jsp.startsWith("/") ? jsp : "/" + jsp;
            req.getRequestDispatcher(path).forward(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/manageReservations");
        }
    }
}