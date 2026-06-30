package controller.api;

import banco.ClienteDAO;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/privada/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        HttpSession session = request.getSession(false);
        Cliente usuarioLogado = (session != null) ? (Cliente) session.getAttribute("usuarioLogado") : null;

        if (usuarioLogado == null) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Usuário não autenticado.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        
        if (nome == null || nome.isEmpty()) {
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Nome não pode ficar vazio.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        try (Connection connection = new DBConnection().getConnection()) {
            String sql = "UPDATE cliente SET nome = ?, telefone = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, telefone);
                stmt.setInt(3, usuarioLogado.getId());
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    usuarioLogado.setNome(nome);
                    usuarioLogado.setTelefone(telefone);
                    session.setAttribute("usuarioLogado", usuarioLogado); // Atualiza sessao
                    
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Perfil atualizado com sucesso!");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Erro ao atualizar perfil no banco.");
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
