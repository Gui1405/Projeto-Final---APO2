package controller.api;

import banco.DBConnection;
import com.google.gson.Gson;
import model.Filme;

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

@WebServlet(urlPatterns = {"/api/filmes", "/api/admin/filmes"})
public class FilmeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        
        List<Filme> filmes = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection()) {
            String sql;
            if (request.getRequestURI().contains("admin")) {
                sql = "SELECT * FROM filme";
            } else {
                sql = "SELECT DISTINCT f.* FROM filme f INNER JOIN sessao s ON f.id = s.filme_id WHERE s.horario >= NOW()";
            }
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Filme f = new Filme();
                    f.setId(rs.getInt("id"));
                    f.setNome(rs.getString("titulo"));
                    f.setDuracao(rs.getInt("duracao"));
                    f.setGenero(rs.getString("genero"));
                    f.setClassificacao(rs.getString("classificacao"));
                    f.setSinopse(rs.getString("sinopse"));
                    filmes.add(f);
                }
            }
            out.print(gson.toJson(filmes));
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

        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "INSERT INTO filme (titulo, duracao, genero, classificacao, sinopse) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, request.getParameter("titulo"));
                stmt.setInt(2, Integer.parseInt(request.getParameter("duracao")));
                stmt.setString(3, request.getParameter("genero"));
                stmt.setString(4, request.getParameter("classificacao"));
                stmt.setString(5, request.getParameter("sinopse"));
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int filmeId = generatedKeys.getInt(1);
                            String salaIdStr = request.getParameter("sala_id");
                            if (salaIdStr != null && !salaIdStr.trim().isEmpty()) {
                                String horario = request.getParameter("horario");
                                String valorStr = request.getParameter("valor_ingresso");
                                if (horario != null && !horario.isEmpty() && valorStr != null && !valorStr.isEmpty()) {
                                    String sqlSessao = "INSERT INTO sessao (filme_id, sala_id, horario, valor_ingresso) VALUES (?, ?, ?, ?)";
                                    try (PreparedStatement stmtSessao = connection.prepareStatement(sqlSessao)) {
                                        stmtSessao.setInt(1, filmeId);
                                        stmtSessao.setInt(2, Integer.parseInt(salaIdStr));
                                        stmtSessao.setString(3, horario);
                                        stmtSessao.setDouble(4, Double.parseDouble(valorStr));
                                        stmtSessao.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Filme cadastrado com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Erro ao cadastrar filme.");
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
            String bodyString = "";
            if (request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")) {
                bodyString = request.getReader().lines().collect(java.util.stream.Collectors.joining(""));
                if (!bodyString.isEmpty()) {
                    for (String pair : bodyString.split("&")) {
                        String[] kv = pair.split("=");
                        if (kv.length == 2) {
                            params.put(java.net.URLDecoder.decode(kv[0], "UTF-8"), java.net.URLDecoder.decode(kv[1], "UTF-8"));
                        } else if (kv.length == 1) {
                            params.put(java.net.URLDecoder.decode(kv[0], "UTF-8"), "");
                        }
                    }
                }
            }
            
            if (params.get("duracao") == null) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Debug -> Params: " + params.toString() + " | Query: " + request.getQueryString() + " | Body: " + bodyString + " | ContentType: " + request.getContentType());
                out.print(gson.toJson(jsonResponse));
                out.flush();
                return;
            }

            String sql = "UPDATE filme SET titulo = ?, duracao = ?, genero = ?, classificacao = ?, sinopse = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, params.get("titulo"));
                stmt.setInt(2, Integer.parseInt(params.get("duracao")));
                stmt.setString(3, params.get("genero"));
                stmt.setString(4, params.get("classificacao"));
                stmt.setString(5, params.get("sinopse"));
                stmt.setInt(6, Integer.parseInt(params.get("id")));
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Filme atualizado com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Filme não encontrado.");
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
            String sql = "DELETE FROM filme WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(request.getParameter("id")));
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Filme apagado com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Filme não encontrado.");
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
