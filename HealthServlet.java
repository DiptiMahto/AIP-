package com.fitnesstracker;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class HealthServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            String gender = request.getParameter("gender");
            int height = Integer.parseInt(request.getParameter("height"));
            int weight = Integer.parseInt(request.getParameter("weight"));
            int targetWeight = Integer.parseInt(request.getParameter("targetWeight"));
            int days = Integer.parseInt(request.getParameter("days"));

            // Validation to ensure positive values
            if (age <= 0 || height <= 0 || weight <= 0 || targetWeight <= 0 || days <= 0) {
                response.getWriter().println("<script>alert('Invalid input. Please enter positive values.'); window.location='healthinfo.html';</script>");
                return;
            }

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnesstracker", "root","anjusus@#234");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO health_info (name, age, gender, height, weight, targetWeight, days) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                stmt.setString(1, name);
                stmt.setInt(2, age);
                stmt.setString(3, gender);
                stmt.setInt(4, height);
                stmt.setInt(5, weight);
                stmt.setInt(6, targetWeight);
                stmt.setInt(7, days);
                stmt.executeUpdate();

                response.sendRedirect("confirmation.html");
            }
        } catch (NumberFormatException e) {
            response.getWriter().println("<script>alert('Please enter valid numeric values.'); window.location='healthinfo.html';</script>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("<script>alert('Database Error! Please try again later.'); window.location='healthinfo.html';</script>");
        }
    }
}