package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Filme;

public class FilmeDAO {

    public List<Filme> listarTodos() {
        List<Filme> listaFilmes = new ArrayList<>();
        String sql = "SELECT * FROM Filme";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Filme filme = new Filme();
                filme.setId(resultSet.getInt("FilmeId"));
                filme.setNome(resultSet.getString("Titulo")); 
                filme.setDuracao(resultSet.getInt("Duracao"));
                filme.setGenero(resultSet.getString("Genero"));
                filme.setClassificacao(resultSet.getString("Classificacao"));
                filme.setSinopse(resultSet.getString("Sinopse"));
                listaFilmes.add(filme);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return listaFilmes;
    }
}