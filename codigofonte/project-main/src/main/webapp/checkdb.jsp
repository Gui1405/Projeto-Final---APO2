<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="banco.DBConnection" %>
<%
    try (Connection conn = new DBConnection().getConnection();
         Statement stmt = conn.createStatement()) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM poltrona WHERE sala_id = 1");
            if (rs.next()) {
                out.println("Poltronas in sala 1: " + rs.getInt(1));
            }
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM poltrona LIMIT 5");
            while (rs2.next()) {
                out.println("Poltrona " + rs2.getString("numero") + " Sala " + rs2.getInt("sala_id"));
            }
        } catch(Exception e) {
            out.println("Erro poltrona: " + e.getMessage());
        }
    } catch (Exception e) {
        e.printStackTrace(new java.io.PrintWriter(out));
    }
%>
