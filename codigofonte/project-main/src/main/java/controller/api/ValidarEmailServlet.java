package controller.api;

import banco.ClienteDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/validar")
public class ValidarEmailServlet extends HttpServlet {

    private ClienteDAO clienteDAO;

    @Override
    public void init() {
        clienteDAO = new ClienteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<html><head><title>Validação de E-mail</title>");
        out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">");
        out.println("<style>body { background-color: #0f172a; color: white; display: flex; align-items: center; justify-content: center; height: 100vh; font-family: 'Outfit', sans-serif; }");
        out.println(".card-glass { background: rgba(30, 41, 59, 0.7); padding: 40px; border-radius: 20px; text-align: center; border: 1px solid rgba(255, 255, 255, 0.1); }</style>");
        out.println("</head><body><div class=\"card-glass\">");

        try {
            if (email != null && !email.isEmpty()) {
                boolean sucesso = clienteDAO.validarEmail(email);
                if (sucesso) {
                    out.println("<h2 style=\"color: #38bdf8;\">Conta ativada com sucesso!</h2>");
                    out.println("<p class=\"mt-3\">Seu e-mail <strong>" + email + "</strong> foi verificado.</p>");
                    out.println("<a href=\"login.jsp\" class=\"btn btn-primary mt-4\">Fazer Login</a>");
                } else {
                    out.println("<h2 class=\"text-danger\">Erro na validação</h2>");
                    out.println("<p class=\"mt-3\">E-mail não encontrado ou já validado.</p>");
                    out.println("<a href=\"login.jsp\" class=\"btn btn-secondary mt-4\">Ir para o Login</a>");
                }
            } else {
                out.println("<h2 class=\"text-danger\">Link inválido</h2>");
                out.println("<p class=\"mt-3\">Nenhum e-mail foi fornecido na URL.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2 class=\"text-danger\">Erro interno</h2>");
            out.println("<p class=\"mt-3\">Ocorreu um erro ao processar sua solicitação. Tente novamente.</p>");
        }
        
        out.println("</div></body></html>");
    }
}
