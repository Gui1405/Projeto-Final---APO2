package controller;

import java.util.List;
import banco.SessaoDAO;
import model.Sessao;
import model.Filme;

public class SessaoController {

    private SessaoDAO sessaoDAO;

    public SessaoController() {
        this.sessaoDAO = new SessaoDAO();
    }

    public List<Sessao> listarPorFilme(Filme filme) {
        // O DAO utiliza o objeto Filme para filtrar
        return sessaoDAO.listarPorFilme(filme);
    }
}