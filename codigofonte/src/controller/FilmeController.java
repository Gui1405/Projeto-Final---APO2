package controller;

import java.util.List;
import banco.FilmeDAO;
import model.Filme;

public class FilmeController {

    private FilmeDAO filmeDAO;

    public FilmeController() {
        this.filmeDAO = new FilmeDAO();
    }

    public List<Filme> listarTodos() {
        return filmeDAO.listarTodos();
    }
}