package br.cinema.service;

import java.util.List;
import br.cinema.model.Filme;
import br.cinema.dao.FilmeDAO;

public class FilmeService {

    private FilmeDAO filmeDao = new FilmeDAO();

    public List<Filme> listarTodos() {
        // Agora a lógica de negócios consome os dados reais do banco
        return filmeDao.listarTodos();
    }
}