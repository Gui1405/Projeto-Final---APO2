package controller.api;

import banco.ClienteDAO;
import com.google.gson.Gson;
import model.Cliente;
import util.EmailSender;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/recuperar")
public class RecuperacaoSenhaServlet extends HttpServlet {

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
            String email = request.getParameter("email");

            if (email == null || email.isEmpty()) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "E-mail obrigatório.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            Cliente cliente = clienteDAO.buscarPorEmail(email);

            if (cliente != null) {
                // Para a POC, define uma senha temporaria '123456'
                boolean atualizado = clienteDAO.atualizarSenha(email, "123456");

                if (atualizado) {
                    new Thread(() -> {
                        EmailSender.enviarEmailRecuperacao(email);
                    }).start();

                    jsonResponse.put("status", "success");
                    jsonResponse.put("message", "Instruções de recuperação foram enviadas para o seu e-mail.");
                } else {
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Erro ao processar recuperação.");
                }
            } else {
                // Retorna sucesso para evitar enumeração de usuários (boa pratica de segurança),
                // ou um erro especifico dependendo do contexto academico. Vamos manter simples:
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "E-mail não encontrado no sistema.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro interno no servidor.");
        } finally {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }
}
