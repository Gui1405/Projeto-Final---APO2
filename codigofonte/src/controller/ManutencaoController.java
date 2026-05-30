package controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import banco.ManutencaoDAO;
import model.Manutencao;
import model.Sala;

public class ManutencaoController {

    private ManutencaoDAO manutencaoDAO;

    public ManutencaoController() {
        this.manutencaoDAO = new ManutencaoDAO();
    }

    public String iniciarManutencao(Sala sala) {
        try {
            Manutencao manutencao = new Manutencao();
            // Associa o objeto recebido
            manutencao.setSala(sala); 
            manutencao.setDataManutencao(LocalDateTime.now());
            manutencao.setStatus("EM MANUTENÇÃO");
            manutencao.setObservacao("Bloqueio preventivo");

            manutencaoDAO.registrarInicio(manutencao);
            return "Manutenção iniciada na Sala " + sala.getId();
        } catch (SQLException e) {
            return "Erro ao iniciar manutenção: " + e.getMessage();
        }
    }

    public String finalizarManutencao(Sala sala) {
        try {
            Manutencao manutencao = new Manutencao();
            manutencao.setSala(sala);
            manutencao.setStatus("FINALIZADA");
            
            manutencaoDAO.registrarFim(manutencao);
            return "Manutenção finalizada na Sala " + sala.getId();
        } catch (SQLException e) {
            return "Erro ao finalizar manutenção: " + e.getMessage();
        }
    }

    public String verHistorico(Sala sala) {
        return manutencaoDAO.buscarHistorico(sala);
    }
}