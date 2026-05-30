package controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import banco.LimpezaDAO;
import model.Limpeza;
import model.Sala;

public class LimpezaController {

    private LimpezaDAO limpezaDAO;

    public LimpezaController() {
        this.limpezaDAO = new LimpezaDAO();
    }

    public String iniciarLimpeza(Sala sala) {
        try {
            Limpeza limpeza = new Limpeza();
            limpeza.setSala(sala); // Associa o objeto Sala recebido
            limpeza.setDataLimpeza(LocalDateTime.now());
            limpeza.setStatus("EM ANDAMENTO");
            limpeza.setObservacao("Início de rotina");

            limpezaDAO.registrarInicio(limpeza); 
            
            return "Limpeza iniciada com sucesso na Sala " + sala.getId();
        } catch (SQLException e) {
            return "Erro ao iniciar limpeza: " + e.getMessage();
        }
    }

    public String finalizarLimpeza(Sala sala) {
        try {
            Limpeza limpeza = new Limpeza();
            limpeza.setSala(sala);
            limpeza.setDataLimpeza(LocalDateTime.now());
            limpeza.setStatus("CONCLUÍDA");

            limpezaDAO.registrarFim(limpeza);
            return "Limpeza finalizada com sucesso na Sala " + sala.getId();
        } catch (SQLException e) {
            return "Erro ao finalizar limpeza: " + e.getMessage();
        }
    }

    public String verHistorico(Sala sala) {
        // Passa o objeto Sala diretamente para o DAO
        return limpezaDAO.buscarHistorico(sala); 
    }
}