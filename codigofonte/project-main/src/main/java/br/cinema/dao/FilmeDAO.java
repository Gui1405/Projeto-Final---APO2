package br.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.cinema.model.Filme;
import br.cinema.util.DBConnection;

public class FilmeDAO {

    public List<Filme> listarTodos() {
        List<Filme> filmes = new ArrayList<>();
        String sql = "SELECT * FROM Filme";
        
        // Utiliza try-with-resources para garantir o fechamento automático da conexao
        try (Connection conexao = DBConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Filme f = new Filme();
                f.setId(rs.getInt("FilmeId"));
                f.setTitulo(rs.getString("Titulo"));
                f.setDuracao(rs.getInt("Duracao"));
                f.setGenero(rs.getString("Genero"));
                f.setClassificacao(rs.getString("Classificacao"));
                f.setSinopse(rs.getString("Sinopse"));
                
                filmes.add(f);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar filmes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return filmes;
    }
}