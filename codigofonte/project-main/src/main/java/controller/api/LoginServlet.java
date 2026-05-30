package controller.api;

import banco.ClienteDAO;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private ClienteDAO clienteDAO;

    @Override
    public void init() {
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // Ler dados JSON da requisicao AJAX (ou form params dependendo de como o jQuery enviar)
            // Assumindo que o front envia form urlencoded padrão pelo jQuery.ajax
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");

            if (email == null || senha == null || email.isEmpty() || senha.isEmpty()) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "E-mail e senha são obrigatórios.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            Cliente cliente = clienteDAO.buscarPorEmail(email);

            if (cliente != null && cliente.getSenha().equals(senha)) {
                if (!cliente.isValidado()) {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Conta não validada. Verifique seu e-mail.");
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioLogado", cliente);
                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Login realizado com sucesso.");
                    if ("ADMIN".equals(cliente.getPerfil())) {
                        jsonResponse.put("redirect", "admin/dashboard.jsp");
                    } else {
                        jsonResponse.put("redirect", "privada/dashboard.jsp");
                    }
                }
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Credenciais inválidas.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro interno no servidor: " + e.toString());
        } finally {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }
}
