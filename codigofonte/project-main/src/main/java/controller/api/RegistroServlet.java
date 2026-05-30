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

@WebServlet("/api/registro")
public class RegistroServlet extends HttpServlet {

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
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String cpf = request.getParameter("cpf");
            String telefone = request.getParameter("telefone");

            if (nome == null || email == null || senha == null || cpf == null) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Preencha todos os campos obrigatórios.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            if (clienteDAO.buscarPorEmail(email) != null) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "E-mail já cadastrado no sistema.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            Cliente novoCliente = new Cliente();
            novoCliente.setNome(nome);
            novoCliente.setEmail(email);
            novoCliente.setSenha(senha); // Ideal: Encriptar a senha (BCrypt) na proxima fase
            novoCliente.setCpf(cpf);
            novoCliente.setTelefone(telefone);
            novoCliente.setValidado(false); // Fica false ate validar o e-mail

            boolean sucesso = clienteDAO.inserir(novoCliente);

            if (sucesso) {
                // Enviar e-mail de validação via Mailtrap
                new Thread(() -> {
                    EmailSender.enviarEmailValidacao(email, nome);
                }).start();

                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Cadastro realizado com sucesso. Verifique seu e-mail para validar a conta.");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Erro ao inserir no banco de dados.");
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
