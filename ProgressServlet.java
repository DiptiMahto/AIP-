package com.fitnesstracker;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class ProgressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitnesstracker", "root", "password");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT date, weight, calories FROM progress")) {

            out.println("<html><head><title>Progress Data</title></head><body>");
            out.println("<h2>Fitness Progress Data</h2>");

            if (!rs.isBeforeFirst()) {
                out.println("<p>No progress data available.</p>");
            } else {
                out.println("<table border='1'><tr><th>Date</th><th>Weight (kg)</th><th>Calories Burned</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getString("date") + "</td><td>" +
                                rs.getInt("weight") + "</td><td>" +
                                rs.getInt("calories") + "</td></tr>");
                }
                out.println("</table>");
            }

            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<script>alert('An error occurred while fetching progress data.'); window.location='error.html';</script>");
        }
    }
}