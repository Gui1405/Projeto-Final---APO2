package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Sala;

public class SalaDAO {

    public Sala buscarPorId(Sala salaFiltro) throws SQLException {
        String sql = "SELECT * FROM Sala WHERE SalaId = ?";
        Sala salaRetorno = null;

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, salaFiltro.getId());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    salaRetorno = new Sala();
                    salaRetorno.setId(resultSet.getInt("SalaId"));
                    salaRetorno.setNumero(resultSet.getInt("NumeroSala")); 
                    salaRetorno.setCapacidade(resultSet.getInt("Capacidade"));
                    salaRetorno.setTipo(resultSet.getString("TipoSala")); 
                    salaRetorno.setDisponivel(resultSet.getBoolean("Disponivel"));
                }
            }
        }
        return salaRetorno;
    }
}