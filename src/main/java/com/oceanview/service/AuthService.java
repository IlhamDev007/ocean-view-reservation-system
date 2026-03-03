package com.oceanview.service;

import com.oceanview.dao.UserDAO;
import com.oceanview.model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String username, String password) throws Exception {
        // basic validation (server-side)
        if (username == null || username.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;

        return userDAO.findByUsernameAndPassword(username.trim(), password.trim());
    }
}