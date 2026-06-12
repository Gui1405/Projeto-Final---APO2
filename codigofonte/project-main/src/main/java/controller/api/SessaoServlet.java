package controller.api;

import banco.DBConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/sessoes")
public class SessaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().create();
        
        String filmeIdParam = request.getParameter("filmeId");
        if (filmeIdParam == null || filmeIdParam.isEmpty()) {
            response.setStatus(400);
            out.print("{\"error\": \"filmeId is required\"}");
            out.flush();
            return;
        }

        List<Map<String, Object>> sessoes = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT s.id as SessaoId, s.horario as HorarioInicio, s.horario as HorarioFim, s.sala_id as SalaId, sl.nome as NumeroSala " +
                         "FROM sessao s " +
                         "JOIN sala sl ON s.sala_id = sl.id " +
                         "WHERE s.filme_id = ?";
                         
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(filmeIdParam));
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> s = new HashMap<>();
                        s.put("id", rs.getInt("SessaoId"));
                        s.put("inicio", rs.getString("HorarioInicio"));
                        s.put("fim", rs.getString("HorarioFim"));
                        s.put("salaId", rs.getInt("SalaId"));
                        s.put("numeroSala", rs.getString("NumeroSala"));
                        sessoes.add(s);
                    }
                }
            }
            out.print(gson.toJson(sessoes));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\": \"Internal server error\"}");
        } finally {
            out.flush();
        }
    }
}
