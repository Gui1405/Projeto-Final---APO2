package controller.api;

import banco.SalaDAO;
import com.google.gson.Gson;
import model.Sala;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/salas")
public class SalaServlet extends HttpServlet {

    private SalaDAO salaDAO;

    @Override
    public void init() {
        salaDAO = new SalaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        try {
            List<Sala> salas = salaDAO.listarTodas();
            out.print(gson.toJson(salas));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
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

        try {
            Sala sala = new Sala();
            sala.setNumero(Integer.parseInt(request.getParameter("numero")));
            sala.setCapacidade(Integer.parseInt(request.getParameter("capacidade")));
            sala.setDisponivel(true); // Nova sala sempre disponível por padrão

            if (salaDAO.inserir(sala)) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Sala cadastrada com sucesso!");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Erro ao cadastrar sala.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro interno: " + e.getMessage());
        } finally {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            Map<String, String> params = new HashMap<>();
            request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));
            if (request.getContentType() != null && request.getContentType().contains("application/x-www-form-urlencoded")) {
                String body = request.getReader().lines().collect(java.util.stream.Collectors.joining(""));
                for (String pair : body.split("&")) {
                    String[] kv = pair.split("=");
                    if (kv.length == 2) {
                        params.put(java.net.URLDecoder.decode(kv[0], "UTF-8"), java.net.URLDecoder.decode(kv[1], "UTF-8"));
                    }
                }
            }

            Sala sala = new Sala();
            sala.setId(Integer.parseInt(params.get("id")));
            sala.setNumero(Integer.parseInt(params.get("numero")));
            sala.setCapacidade(Integer.parseInt(params.get("capacidade")));
            sala.setDisponivel("true".equalsIgnoreCase(params.get("disponivel")));

            if (salaDAO.atualizar(sala)) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Sala atualizada com sucesso!");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Sala não encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro interno: " + e.getMessage());
        } finally {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (salaDAO.deletar(id)) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Sala apagada com sucesso!");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Sala não encontrada.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Erro interno: " + e.getMessage());
        } finally {
            out.print(gson.toJson(jsonResponse));
            out.flush();
        }
    }
}
