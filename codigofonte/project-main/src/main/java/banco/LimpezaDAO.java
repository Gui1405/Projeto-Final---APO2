package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Limpeza;
import model.Sala;

public class LimpezaDAO {

    public void registrarInicio(Limpeza limpeza) throws SQLException {
        String sql = "CALL sp_IniciarLimpeza(?)";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, limpeza.getSala().getId());
            preparedStatement.execute();
        }
    }

    public void registrarFim(Limpeza limpeza) throws SQLException {
        String sql = "CALL sp_FinalizarLimpeza(?)";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, limpeza.getSala().getId());
            preparedStatement.execute();
        }
    }

    public String buscarUltimoStatus(Sala sala) {
        String status = null;
        String sql = "SELECT status FROM limpeza WHERE sala_id = ? ORDER BY data_limpeza DESC LIMIT 1";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setInt(1, sala.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    status = resultSet.getString("status");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return status;
    }

    public List<Limpeza> buscarHistoricoPorSala(Sala sala) {
        List<Limpeza> historico = new ArrayList<>();
        String sql = "SELECT * FROM limpeza WHERE sala_id = ? ORDER BY data_limpeza DESC";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, sala.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Limpeza limpeza = new Limpeza();
                    limpeza.setId(resultSet.getInt("id"));
                    limpeza.setData(resultSet.getTimestamp("data_limpeza").toLocalDateTime());
                    limpeza.setStatus(resultSet.getString("status"));
                    limpeza.setObservacao(resultSet.getString("observacao"));
                    limpeza.setSala(sala);

                    historico.add(limpeza);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return historico;
    }
}