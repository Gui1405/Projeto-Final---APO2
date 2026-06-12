package controller.api;

import banco.PoltronaDAO;
import com.google.gson.Gson;
import model.Poltrona;
import model.Sala;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/poltronas")
public class PoltronaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        
        String salaIdParam = request.getParameter("salaId");
        if (salaIdParam == null || salaIdParam.isEmpty()) {
            response.setStatus(400);
            out.print("{\"error\": \"salaId is required\"}");
            out.flush();
            return;
        }

        try {
            int salaId = Integer.parseInt(salaIdParam);
            Sala sala = new Sala();
            sala.setId(salaId);

            PoltronaDAO poltronaDAO = new PoltronaDAO();
            List<Poltrona> poltronas = poltronaDAO.listarPorSala(sala);

            List<Map<String, Object>> result = new ArrayList<>();
            for (Poltrona p : poltronas) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", p.getId());
                map.put("numero", p.getNumero());
                map.put("disponivel", p.getDisponivel());
                result.add(map);
            }

            out.print(gson.toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("{\"error\": \"Internal server error\"}");
        } finally {
            out.flush();
        }
    }
}
