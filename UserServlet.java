package com.fitnesstracker;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class UserServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/fitness_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "anjusus@#234";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equalsIgnoreCase(action)) {
            handleLogin(request, response);
        } else if ("register".equalsIgnoreCase(action)) {
            handleRegistration(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    response.sendRedirect("entry.html");
                } else {
                    response.getWriter().println("<script>alert('Login Failed. Invalid username or password.'); window.location='login.html';</script>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('Database Error! Please try again later.'); window.location='login.html';</script>");
        }
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            response.getWriter().println("<script>alert('Registration Successful! Please login.'); window.location='login.html';</script>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('Registration Failed. Username may already exist.'); window.location='register.html';</script>");
        }
    }
}