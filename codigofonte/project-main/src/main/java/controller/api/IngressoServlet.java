package controller.api;

import banco.IngressoDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Cliente;
import model.Ingresso;
import model.Poltrona;
import model.Sessao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/privada/ingressos")
public class IngressoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> jsonResponse = new HashMap<>();

        HttpSession session = request.getSession(false);
        Cliente usuarioLogado = (session != null) ? (Cliente) session.getAttribute("usuarioLogado") : null;
        if (usuarioLogado == null) {
            response.setStatus(401);
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Usuário não autenticado.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        try {
            BufferedReader reader = request.getReader();
            Map<String, Object> body = gson.fromJson(reader, new TypeToken<Map<String, Object>>(){}.getType());

            int sessaoId = ((Double) body.get("sessaoId")).intValue();
            List<Double> poltronasDouble = (List<Double>) body.get("poltronas");

            IngressoDAO ingressoDAO = new IngressoDAO();
            Sessao sessao = new Sessao();
            sessao.setId(sessaoId);

            boolean hasError = false;
            String errorMessage = "";

            for (Double pIdDouble : poltronasDouble) {
                int poltronaId = pIdDouble.intValue();
                
                Ingresso ingresso = new Ingresso();
                ingresso.setCliente(usuarioLogado);
                ingresso.setSessao(sessao);
                
                Poltrona poltrona = new Poltrona();
                poltrona.setId(poltronaId);
                ingresso.setPoltrona(poltrona);
                
                ingresso.setStatus("COMPRADO");

                ingresso = ingressoDAO.inserir(ingresso);
                
                if (ingresso.getId() == -1) {
                    hasError = true;
                    errorMessage = "Uma ou mais poltronas selecionadas já estão ocupadas.";
                    break;
                } else if (ingresso.getId() == -2) {
                    hasError = true;
                    errorMessage = "A sala está indisponível (manutenção ou limpeza).";
                    break;
                }
            }

            if (hasError) {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", errorMessage);
            } else {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Ingressos comprados com sucesso!");
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
