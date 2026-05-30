package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Limpeza;
import model.Sala;

public class LimpezaDAO {

    public void registrarInicio(Limpeza limpeza) throws SQLException {
        String sql = "{call sp_IniciarLimpeza(?)}";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, limpeza.getSala().getId());
            preparedStatement.execute();
        }
    }

    public void registrarFim(Limpeza limpeza) throws SQLException {
        String sql = "{call sp_FinalizarLimpeza(?)}";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, limpeza.getSala().getId());
            preparedStatement.execute();
        }
    }

    public String buscarUltimoStatus(Sala sala) {
        String sql = "SELECT StatusLimpeza FROM Limpeza WHERE SalaId = ? ORDER BY LimpezaId DESC LIMIT 1";
        String status = "Desconhecido";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             
            preparedStatement.setInt(1, sala.getId());
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    status = resultSet.getString("StatusLimpeza");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return status;
    }

    public String buscarHistorico(Sala sala) {
        StringBuilder stringBuilder = new StringBuilder();
        String sql = "SELECT * FROM Limpeza WHERE SalaId = ?";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, sala.getId());
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    stringBuilder.append("Data: ").append(resultSet.getObject("DataLimpeza"))
                      .append(" - Status: ").append(resultSet.getString("StatusLimpeza"))
                      .append("\n");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return "Erro ao buscar histórico.";
        }
        return stringBuilder.length() > 0 ? stringBuilder.toString() : "Nenhum registro encontrado.";
    }
}