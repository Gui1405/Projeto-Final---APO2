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
            String sql = "SELECT * FROM filme";
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
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, request.getParameter("titulo"));
                stmt.setInt(2, Integer.parseInt(request.getParameter("duracao")));
                stmt.setString(3, request.getParameter("genero"));
                stmt.setString(4, request.getParameter("classificacao"));
                stmt.setString(5, request.getParameter("sinopse"));
                
                int affected = stmt.executeUpdate();
                if (affected > 0) {
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
}
