package com.oceanview.servlet;

import com.oceanview.service.AuthService;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Handle GET requests - Render the login page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // Handle POST requests - Perform login authentication
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve username and password from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate credentials using AuthService
        AuthService authService = new AuthService();
        User user = authService.authenticate(username, password);

        if (user != null) {
            // Valid user, create a session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect based on user role (Admin or Staff)
            if ("ADMIN".equals(user.getRole())) {
                response.sendRedirect("admin/dashboard.jsp");
            } else if ("STAFF".equals(user.getRole())) {
                response.sendRedirect("staff/dashboard.jsp");
            }
        } else {
            // Invalid credentials
            response.sendRedirect("login.jsp?error=Invalid credentials");
        }
    }
}