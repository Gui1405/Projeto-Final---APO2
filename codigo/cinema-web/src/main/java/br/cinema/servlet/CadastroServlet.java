package br.cinema.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.cinema.dao.ClienteDAO;
import br.cinema.model.Cliente;

@WebServlet("/cadastro")
public class CadastroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClienteDAO clienteDao = new ClienteDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Define o retorno como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Captura os parâmetros enviados pelo formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String telefone = request.getParameter("telefone");
        String senha = request.getParameter("senha");
        
        // Validação básica de campos em branco
        if (nome == null || email == null || telefone == null || senha == null || 
            nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty()) {
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"erro\", \"mensagem\":\"Todos os campos são obrigatórios.\"}");
            return;
        }
        
        // Preenche o objeto Cliente
        Cliente novoCliente = new Cliente();
        novoCliente.setNome(nome);
        novoCliente.setEmail(email);
        novoCliente.setTelefone(telefone);
        novoCliente.setSenha(senha);
        
        // Tenta salvar no banco de dados
        boolean sucesso = clienteDao.cadastrar(novoCliente);
        
        if (sucesso) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"status\":\"sucesso\", \"mensagem\":\"Cadastro realizado com sucesso!\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"erro\", \"mensagem\":\"Erro ao cadastrar. O e-mail pode já estar em uso.\"}");
        }
    }
}