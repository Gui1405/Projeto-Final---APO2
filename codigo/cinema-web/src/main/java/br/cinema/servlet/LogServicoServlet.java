package br.cinema.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import br.cinema.dao.LogServicoDAO;
import br.cinema.model.ServicoLog;

@WebServlet("/logs-servicos")
public class LogServicoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LogServicoDAO logDao = new LogServicoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<ServicoLog> logs = logDao.listarTodosLogs();
        String json = new Gson().toJson(logs);
        
        response.getWriter().write(json);
    }
}