package br.cinema.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import br.cinema.model.Cliente;

// O WebFilter define quais URLs serão interceptadas. 
// O asterisco (*) garante que qualquer arquivo dentro da pasta admin passe por aqui.
@WebFilter("/jsp/admin/*")
public class AutenticacaoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Executado apenas uma vez quando o servidor inicia
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // Pega a sessão atual, mas não cria uma nova se não existir (false)
        HttpSession sessao = req.getSession(false);

        boolean estaLogado = (sessao != null && sessao.getAttribute("usuarioLogado") != null);

        if (estaLogado) {
            Cliente usuario = (Cliente) sessao.getAttribute("usuarioLogado");
            
            // Camada adicional de segurança: verifica se a rota possui '/admin/' 
            // e bloqueia clientes comuns de acessarem, redirecionando para a home
            if (req.getRequestURI().contains("/admin/") && !"ADMIN".equals(usuario.getPerfil())) {
                res.sendRedirect(req.getContextPath() + "/jsp/index.jsp");
                return;
            }
            
            // O usuário tem permissão. O chain.doFilter libera o acesso à página solicitada.
            chain.doFilter(request, response);
            
        } else {
            // O usuário não está logado. Redireciona imediatamente para a tela de login.
            res.sendRedirect(req.getContextPath() + "/jsp/login.jsp");
        }
    }

    @Override
    public void destroy() {
        // Executado quando o servidor é desligado
    }
}