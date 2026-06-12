package controller.api;

import banco.ManutencaoDAO;
import banco.SalaDAO;
import com.google.gson.Gson;
import model.Cliente;
import model.Manutencao;
import model.Sala;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/manutencao")
public class ManutencaoServlet extends HttpServlet {

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        Cliente usuario = (Cliente) session.getAttribute("usuarioLogado");
        return usuario != null && "ADMIN".equals(usuario.getPerfil());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        if (!isAdmin(request)) {
            response.setStatus(403);
            out.print("{\"error\": \"Acesso negado.\"}");
            return;
        }

        try {
            SalaDAO salaDAO = new SalaDAO();
            ManutencaoDAO manutencaoDAO = new ManutencaoDAO();
            List<Sala> salas = salaDAO.listarTodas();
            List<Map<String, Object>> result = new ArrayList<>();

            for (Sala s : salas) {
                Map<String, Object> map = new HashMap<>();
                map.put("salaId", s.getId());
                map.put("numero", s.getNumero());
                map.put("disponivel", s.getDisponivel());
                map.put("statusManutencao", manutencaoDAO.buscarUltimoStatus(s));
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        if (!isAdmin(request)) {
            response.setStatus(403);
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Acesso negado.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        try {
            int salaId = Integer.parseInt(request.getParameter("salaId"));
            String acao = request.getParameter("acao");

            Sala sala = new Sala();
            sala.setId(salaId);

            Manutencao manutencao = new Manutencao();
            manutencao.setSala(sala);

            ManutencaoDAO manutencaoDAO = new ManutencaoDAO();

            if ("iniciar".equals(acao)) {
                manutencaoDAO.registrarInicio(manutencao);
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Manutenção iniciada. Sala bloqueada.");
            } else if ("finalizar".equals(acao)) {
                manutencaoDAO.registrarFim(manutencao);
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Manutenção finalizada. Sala liberada.");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Ação inválida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro: " + e.getMessage());
        } finally {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }
}
