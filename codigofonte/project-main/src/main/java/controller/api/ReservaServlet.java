package controller.api;

import banco.DBConnection;
import com.google.gson.Gson;
import model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/privada/reservas")
public class ReservaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        
        HttpSession session = request.getSession(false);
        Cliente usuarioLogado = (session != null) ? (Cliente) session.getAttribute("usuarioLogado") : null;
        if(usuarioLogado == null) return;

        List<Map<String, Object>> reservas = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT r.id, f.titulo, s.horario, r.quantidade_ingressos, r.valor_total, r.data_reserva " +
                         "FROM reserva r " +
                         "JOIN sessao s ON r.sessao_id = s.id " +
                         "JOIN filme f ON s.filme_id = f.id " +
                         "WHERE r.cliente_id = ? ORDER BY r.data_reserva DESC";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, usuarioLogado.getId());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> r = new HashMap<>();
                        r.put("id", rs.getInt("id"));
                        r.put("filme", rs.getString("titulo"));
                        r.put("horario", rs.getString("horario"));
                        r.put("quantidade", rs.getInt("quantidade_ingressos"));
                        r.put("total", rs.getDouble("valor_total"));
                        r.put("data", rs.getString("data_reserva"));
                        reservas.add(r);
                    }
                }
            }
            out.print(gson.toJson(reservas));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        HttpSession session = request.getSession(false);
        Cliente usuarioLogado = (session != null) ? (Cliente) session.getAttribute("usuarioLogado") : null;
        if(usuarioLogado == null) return;

        try (Connection connection = new DBConnection().getConnection()) {
            int sessaoId = Integer.parseInt(request.getParameter("sessaoId"));
            int quantidade = Integer.parseInt(request.getParameter("quantidade"));
            
            // Busca valor do ingresso na sessao
            double valorIngresso = 0;
            String sqlSessao = "SELECT valor_ingresso FROM sessao WHERE id = ?";
            try(PreparedStatement stmtSessao = connection.prepareStatement(sqlSessao)) {
                stmtSessao.setInt(1, sessaoId);
                try(ResultSet rs = stmtSessao.executeQuery()) {
                    if(rs.next()) valorIngresso = rs.getDouble("valor_ingresso");
                }
            }
            
            double total = valorIngresso * quantidade;

            String sql = "INSERT INTO reserva (cliente_id, sessao_id, quantidade_ingressos, valor_total) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, usuarioLogado.getId());
                stmt.setInt(2, sessaoId);
                stmt.setInt(3, quantidade);
                stmt.setDouble(4, total);
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Reserva efetuada com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Erro ao efetuar reserva.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro interno: " + e.getMessage());
        } finally {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }
}
