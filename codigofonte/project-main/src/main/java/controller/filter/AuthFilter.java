package controller.filter;

import model.Cliente;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/privada/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        
        boolean isLogado = (session != null && session.getAttribute("usuarioLogado") != null);

        if (isLogado) {
            Cliente usuario = (Cliente) session.getAttribute("usuarioLogado");
            
            // Controle de nivel de acesso
            if (requestURI.contains("/admin/") && !"ADMIN".equals(usuario.getPerfil())) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado. Você não tem privilégios de administrador.");
                return;
            }
            
            // Se logado e tem acesso, segue a vida
            chain.doFilter(request, response);
        } else {
            // Nao logado, redireciona pro login publico
            // Se for chamada de API/AJAX, pode ser melhor retornar um JSON 401. 
            // Para JSPs, redireciona. 
            if (requestURI.contains("/api/")) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"status\":\"error\", \"message\":\"Não autorizado.\"}");
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
