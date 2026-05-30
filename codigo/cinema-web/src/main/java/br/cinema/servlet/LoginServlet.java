package br.cinema.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.cinema.dao.ClienteDAO;
import br.cinema.model.Cliente;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClienteDAO clienteDao = new ClienteDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Cliente clienteAutenticado = clienteDao.autenticar(email, senha);
        
        if (clienteAutenticado != null) {
            // Cria a sessão e guarda o objeto do usuário
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", clienteAutenticado);
            
            response.getWriter().write("{\"status\":\"sucesso\", \"perfil\":\"" + clienteAutenticado.getPerfil() + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Erro 401
            response.getWriter().write("{\"status\":\"erro\", \"mensagem\":\"E-mail ou senha incorretos.\"}");
        }
    }
}