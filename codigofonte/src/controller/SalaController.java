package controller;

import java.sql.SQLException;
import banco.SalaDAO;
import model.Sala;

public class SalaController {

    private SalaDAO salaDAO;

    public SalaController() {
        this.salaDAO = new SalaDAO();
    }

    public Sala buscarSala(Sala salaAlvo) {
        try {
            return salaDAO.buscarPorId(salaAlvo);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    public String verificarStatusSala(Sala salaAlvo) {
        try {
            Sala salaEncontrada = salaDAO.buscarPorId(salaAlvo);
            
            if (salaEncontrada != null) {
                String disponibilidade = (salaEncontrada.getDisponivel() != null && salaEncontrada.getDisponivel()) ? "SIM" : "NÃO";
                return "Sala " + salaEncontrada.getNumero() + " - Disponível: " + disponibilidade;
            }
            return "Sala não encontrada.";
        } catch (SQLException exception) {
            return "Erro: " + exception.getMessage();
        }
    }
}