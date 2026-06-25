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

        List<Map<String, Object>> ingressos = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "SELECT i.id as ingresso_id, f.titulo, s.horario, s.valor_ingresso, p.numero as poltrona, i.data_compra, i.status " +
                         "FROM ingresso i " +
                         "JOIN sessao s ON i.sessao_id = s.id " +
                         "JOIN filme f ON s.filme_id = f.id " +
                         "JOIN poltrona p ON i.poltrona_id = p.id " +
                         "WHERE i.cliente_id = ? " +
                         "ORDER BY i.data_compra DESC";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, usuarioLogado.getId());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> r = new HashMap<>();
                        r.put("id", rs.getInt("ingresso_id"));
                        r.put("filme", rs.getString("titulo"));
                        r.put("horario", rs.getString("horario"));
                        r.put("poltrona", rs.getString("poltrona"));
                        r.put("valor", rs.getDouble("valor_ingresso"));
                        r.put("data", rs.getString("data_compra"));
                        r.put("status", rs.getString("status"));
                        ingressos.add(r);
                    }
                }
            }
            out.print(gson.toJson(ingressos));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        HttpSession session = request.getSession(false);
        Cliente usuarioLogado = (session != null) ? (Cliente) session.getAttribute("usuarioLogado") : null;
        if(usuarioLogado == null) return;

        try (Connection connection = new DBConnection().getConnection()) {
            int ingressoId = Integer.parseInt(request.getParameter("id"));

            // Check if ticket belongs to user
            boolean isOwner = false;
            String checkSql = "SELECT id FROM ingresso WHERE id = ? AND cliente_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, ingressoId);
                checkStmt.setInt(2, usuarioLogado.getId());
                try(ResultSet rs = checkStmt.executeQuery()) {
                    if(rs.next()) isOwner = true;
                }
            }

            if(!isOwner) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Acesso negado ou ingresso não existe.");
                out.print(gson.toJson(jsonResponse));
                out.flush();
                return;
            }

            String sql = "{call sp_CancelarReserva(?, ?)}";
            try (java.sql.CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setInt(1, ingressoId);
                callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
                callableStatement.execute();
                String result = callableStatement.getString(2);
                
                if ("Sucesso".equals(result)) {
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Ingresso cancelado e valor reembolsado com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", result);
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
