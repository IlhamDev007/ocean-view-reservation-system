package com.oceanview.dao;

import com.oceanview.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class UserDAOTest {

    private PrintStream originalOut;

    @Before
    public void muteSystemOut() {
        // Save original System.out
        originalOut = System.out;

        // Redirect System.out to nowhere (silence DBConnection prints)
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
    }

    @After
    public void restoreSystemOut() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    /**
     * Test Case: Valid credentials
     * Expected Result: User object is returned (NOT NULL)
     */
    @Test
    public void testLoginSuccess() throws Exception {
        UserDAO dao = new UserDAO();

        User user = dao.findByUsernameAndPassword("admin", "admin123");
        boolean result = (user != null);

        assertTrue("Expected login success for valid credentials", result);
        originalOut.println("Login Success: " + result);
    }

    /**
     * Test Case: Invalid credentials
     * Expected Result: NULL is returned
     */
    @Test
    public void testLoginInvalidUser() throws Exception {
        UserDAO dao = new UserDAO();

        User user = dao.findByUsernameAndPassword("wrongUser", "wrongPass");
        boolean result = (user == null);

        assertTrue("Expected login failure for invalid credentials", result);
        originalOut.println("Login Invalid User: " + result);
    }

    /**
     * Test Case: Empty username
     * Expected Result: NULL is returned
     */
    @Test
    public void testLoginEmptyUsername() throws Exception {
        UserDAO dao = new UserDAO();

        User user = dao.findByUsernameAndPassword("", "admin123");
        boolean result = (user == null);

        assertTrue("Expected login failure for empty username", result);
        originalOut.println("Login Empty Username: " + result);
    }

    /**
     * Test Case: Empty password
     * Expected Result: NULL is returned
     */
    @Test
    public void testLoginEmptyPassword() throws Exception {
        UserDAO dao = new UserDAO();

        User user = dao.findByUsernameAndPassword("admin", "");
        boolean result = (user == null);

        assertTrue("Expected login failure for empty password", result);
        originalOut.println("Login Empty Password: " + result);
    }
}