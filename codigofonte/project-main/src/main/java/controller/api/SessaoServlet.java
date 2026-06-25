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

@WebServlet(urlPatterns = {"/api/sessoes", "/api/admin/sessoes"})
public class SessaoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new GsonBuilder().create();
        
        String filmeIdParam = request.getParameter("filmeId");
        List<Map<String, Object>> sessoes = new ArrayList<>();
        
        try (Connection connection = new DBConnection().getConnection()) {
            String sql;
            PreparedStatement stmt;
            
            if (filmeIdParam != null && !filmeIdParam.isEmpty()) {
                sql = "SELECT s.id as SessaoId, s.horario, s.valor_ingresso, s.sala_id as SalaId, sl.nome as NumeroSala, f.titulo as FilmeTitulo " +
                      "FROM sessao s " +
                      "JOIN sala sl ON s.sala_id = sl.id " +
                      "JOIN filme f ON s.filme_id = f.id " +
                      "WHERE s.filme_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(filmeIdParam));
            } else {
                sql = "SELECT s.id as SessaoId, s.horario, s.valor_ingresso, s.sala_id as SalaId, sl.nome as NumeroSala, s.filme_id as FilmeId, f.titulo as FilmeTitulo " +
                      "FROM sessao s " +
                      "JOIN sala sl ON s.sala_id = sl.id " +
                      "JOIN filme f ON s.filme_id = f.id";
                stmt = connection.prepareStatement(sql);
            }
                         
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> s = new HashMap<>();
                    s.put("id", rs.getInt("SessaoId"));
                    s.put("horario", rs.getString("horario"));
                    s.put("valor_ingresso", rs.getDouble("valor_ingresso"));
                    s.put("salaId", rs.getInt("SalaId"));
                    s.put("numeroSala", rs.getString("NumeroSala"));
                    s.put("filmeTitulo", rs.getString("FilmeTitulo"));
                    if (filmeIdParam == null || filmeIdParam.isEmpty()) {
                        s.put("filmeId", rs.getInt("FilmeId"));
                    }
                    sessoes.add(s);
                }
            }
            stmt.close();
            out.print(gson.toJson(sessoes));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\": \"Internal server error\"}");
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

        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "INSERT INTO sessao (filme_id, sala_id, horario, valor_ingresso) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(request.getParameter("filme_id")));
                stmt.setInt(2, Integer.parseInt(request.getParameter("sala_id")));
                stmt.setString(3, request.getParameter("horario"));
                stmt.setDouble(4, Double.parseDouble(request.getParameter("valor_ingresso")));
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Sessão cadastrada com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Erro ao cadastrar sessão.");
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        try (Connection connection = new DBConnection().getConnection()) {
            Map<String, String> params = new HashMap<>();
            request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
            if (request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")) {
                String body = request.getReader().lines().collect(java.util.stream.Collectors.joining(""));
                for (String pair : body.split("&")) {
                    String[] kv = pair.split("=");
                    if (kv.length == 2) {
                        params.put(java.net.URLDecoder.decode(kv[0], "UTF-8"), java.net.URLDecoder.decode(kv[1], "UTF-8"));
                    }
                }
            }

            String sql = "UPDATE sessao SET filme_id = ?, sala_id = ?, horario = ?, valor_ingresso = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(params.get("filme_id")));
                stmt.setInt(2, Integer.parseInt(params.get("sala_id")));
                stmt.setString(3, params.get("horario"));
                stmt.setDouble(4, Double.parseDouble(params.get("valor_ingresso")));
                stmt.setInt(5, Integer.parseInt(params.get("id")));
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Sessão atualizada com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Sessão não encontrada.");
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "DELETE FROM sessao WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(request.getParameter("id")));
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Sessão apagada com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Sessão não encontrada.");
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
