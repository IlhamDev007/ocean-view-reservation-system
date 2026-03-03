package com.oceanview.controller;

import com.oceanview.model.User;
import com.oceanview.service.AuthService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            User user = authService.login(username, password);

            if (user != null) {
                req.getSession().setAttribute("loggedUser", user.getUsername());
                resp.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                req.setAttribute("error", "Invalid username or password!");
                RequestDispatcher rd = req.getRequestDispatcher("login.jsp");
                rd.forward(req, resp);
            }

        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
            try {
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            } catch (Exception ex) {
                resp.sendRedirect("login.jsp");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("login.jsp");
    }
}