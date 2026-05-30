package banco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Sessao;
import model.Sala;
import model.Filme;

public class SessaoDAO {

    public List<Sessao> listarPorFilme(Filme filmeFiltro) {
        List<Sessao> listaSessoes = new ArrayList<>();
        String sql = "SELECT * FROM Sessao WHERE FilmeId = ?";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, filmeFiltro.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Sessao sessao = new Sessao();
                sessao.setId(resultSet.getInt("SessaoId"));
                sessao.setInicio(resultSet.getTimestamp("HorarioInicio").toLocalDateTime());
                sessao.setFim(resultSet.getTimestamp("HorarioFim").toLocalDateTime());
                
                Sala sala = new Sala();
                sala.setId(resultSet.getInt("SalaId"));
                sessao.setSala(sala);

                Filme filme = new Filme();
                filme.setId(resultSet.getInt("FilmeId"));
                sessao.setFilme(filme);

                listaSessoes.add(sessao);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return listaSessoes;
    }
}