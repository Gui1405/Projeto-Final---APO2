package br.cinema.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import br.cinema.model.Filme;
import br.cinema.service.FilmeService;

@WebServlet("/filmes")
public class FilmeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FilmeService filmeService = new FilmeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Define que o retorno será um JSON tratado em UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Busca a lista vinda da sua lógica de negócio reaproveitada
        List<Filme> listaFilmes = filmeService.listarTodos();
        
        // Transforma o objeto Java em String JSON e envia de volta
        Gson gson = new Gson();
        String json = gson.toJson(listaFilmes);
        
        response.getWriter().write(json);
    }
}