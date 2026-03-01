package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;
import com.oceanview.util.PasswordUtil;

public class AuthService {

    public User authenticate(String username, String password) {
        // Use UserDAO to check the user in the database
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username);

        if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user; // Return user if credentials are valid
        } else {
            return null; // Invalid credentials
        }
    }
}