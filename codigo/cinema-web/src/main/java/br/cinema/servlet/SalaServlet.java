package br.cinema.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import br.cinema.dao.SalaDAO;
import br.cinema.model.Sala;

@WebServlet("/salas")
public class SalaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SalaDAO salaDao = new SalaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<Sala> salas = salaDao.listarTodas();
        String json = new Gson().toJson(salas);
        
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            int salaId = Integer.parseInt(request.getParameter("salaId"));
            String acao = request.getParameter("acao");
            
            boolean sucesso = salaDao.executarServicoSala(salaId, acao);
            
            if (sucesso) {
                response.getWriter().write("{\"status\":\"sucesso\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"status\":\"erro\", \"mensagem\":\"Falha ao executar a operacao na base de dados.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"erro\", \"mensagem\":\"Parametros invalidos.\"}");
        }
    }
}