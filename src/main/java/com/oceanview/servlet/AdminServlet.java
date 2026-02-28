package com.oceanview.servlet;

import com.oceanview.service.RoomService;
import com.oceanview.service.ReservationService;
import com.oceanview.service.UserService;
import com.oceanview.service.ReportService;
import com.oceanview.model.Room;
import com.oceanview.model.Reservation;
import com.oceanview.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    // Handle GET requests - Display admin dashboard and manage actions
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // View all users (for admin management)
        if ("viewUsers".equals(action)) {
            viewUsers(request, response);
        }
        // Add a new user (redirect to add user page)
        else if ("addUser".equals(action)) {
            response.sendRedirect("admin/add-user.jsp");
        }
        // Edit an existing user (redirect to edit user page)
        else if ("editUser".equals(action)) {
            editUser(request, response);
        }
        // View all rooms (for admin management)
        else if ("viewRooms".equals(action)) {
            viewRooms(request, response);
        }
        // Add a new room (redirect to add room page)
        else if ("addRoom".equals(action)) {
            response.sendRedirect("admin/add-room.jsp");
        }
        // View all reservations
        else if ("viewReservations".equals(action)) {
            viewReservations(request, response);
        }
        // Default action: Display the dashboard
        else {
            displayDashboard(request, response);
        }
    }

    // Handle POST requests - Add, Update, or Delete rooms and users
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Add a new room (with image upload)
        if ("addRoom".equals(action)) {
            addRoom(request, response);
        }
        // Update room information
        else if ("updateRoom".equals(action)) {
            updateRoom(request, response);
        }
        // Delete a room from the system
        else if ("deleteRoom".equals(action)) {
            deleteRoom(request, response);
        }
        // Add a new user (staff)
        else if ("addUser".equals(action)) {
            addUser(request, response);
        }
        // Update an existing user
        else if ("updateUser".equals(action)) {
            updateUser(request, response);
        }
        // Delete a user from the system
        else if ("deleteUser".equals(action)) {
            deleteUser(request, response);
        }
    }

    // Display the Admin Dashboard with statistics
    private void displayDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReportService reportService = new ReportService();
        int totalUsers = reportService.getTotalUsers();
        int totalRooms = reportService.getTotalRooms();
        int totalReservations = reportService.getTotalReservations();
        double monthlyRevenue = reportService.getMonthlyRevenue();

        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("totalRooms", totalRooms);
        request.setAttribute("totalReservations", totalReservations);
        request.setAttribute("monthlyRevenue", monthlyRevenue);

        request.getRequestDispatcher("/WEB-INF/views/admin/admin-dashboard.jsp").forward(request, response);
    }

    // View all users (Admin Panel)
    private void viewUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = new UserService();
        List<User> users = userService.getAllUsers(); // Fetch all users
        request.setAttribute("users", users);

        request.getRequestDispatcher("/WEB-INF/views/admin/manage-users.jsp").forward(request, response);
    }

    // Add a new staff user to the system
    private void addUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        UserService userService = new UserService();
        User user = new User(username, password, role); // Create a new user
        userService.addUser(user); // Add the user

        response.sendRedirect("admin?success=User added successfully.");
    }

    // Update an existing staff user's information
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        UserService userService = new UserService();
        userService.updateUser(userId, username, password, role); // Update the user details

        response.sendRedirect("admin?success=User updated successfully.");
    }

    // Delete a staff user from the system
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        UserService userService = new UserService();
        userService.deleteUser(userId); // Delete the user

        response.sendRedirect("admin?success=User deleted successfully.");
    }

    // View all rooms (Admin Panel)
    private void viewRooms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RoomService roomService = new RoomService();
        List<Room> rooms = roomService.getAllRooms(null, null); // Fetch all rooms
        request.setAttribute("rooms", rooms);

        request.getRequestDispatcher("/WEB-INF/views/admin/manage-rooms.jsp").forward(request, response);
    }

    // Add a new room (with images)
    private void addRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roomType = request.getParameter("roomType");
        double price = Double.parseDouble(request.getParameter("price"));
        boolean availability = Boolean.parseBoolean(request.getParameter("availability"));

        RoomService roomService = new RoomService();
        Room room = new Room(roomType, price, availability);

        // Handle image uploads (if any)
        // Add the images to the room
        if (roomService.addRoom(room)) {
            // Handle the uploading of room images
            // Assuming images are handled by File upload or an ImageService

            response.sendRedirect("admin?success=Room added successfully.");
        } else {
            response.sendRedirect("admin?error=Failed to add room.");
        }
    }

    // Update room information
    private void updateRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        String roomType = request.getParameter("roomType");
        double price = Double.parseDouble(request.getParameter("price"));

        RoomService roomService = new RoomService();
        roomService.updateRoom(roomId, roomType, price); // Update the room

        response.sendRedirect("admin?success=Room updated successfully.");
    }

    // Delete a room from the system
    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int roomId = Integer.parseInt(request.getParameter("roomId"));
        RoomService roomService = new RoomService();
        roomService.deleteRoom(roomId); // Delete the room

        response.sendRedirect("admin?success=Room deleted successfully.");
    }

    // View all reservations (Admin Panel)
    private void viewReservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationService reservationService = new ReservationService();
        List<Reservation> reservations = reservationService.getAllReservations(null, null); // Fetch all reservations
        request.setAttribute("reservations", reservations);

        request.getRequestDispatcher("/WEB-INF/views/admin/all-reservations.jsp").forward(request, response);
    }

    // Generate Reports (e.g., Revenue, Reservations, etc.)
    private void generateReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReportService reportService = new ReportService();
        String reportType = request.getParameter("reportType");
        String reportData = reportService.generateReport(reportType);

        request.setAttribute("reportData", reportData);
        request.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(request, response);
    }
}