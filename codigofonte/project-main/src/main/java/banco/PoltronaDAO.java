package banco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Poltrona;
import model.Sala;

public class PoltronaDAO {

    public List<Poltrona> listarPorSala(Sala salaFiltro) {
        List<Poltrona> listaPoltronas = new ArrayList<>();
        String sql = "SELECT * FROM Poltrona WHERE SalaId = ?";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, salaFiltro.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Poltrona poltrona = new Poltrona();
                poltrona.setId(resultSet.getInt("PoltronaId"));
                poltrona.setNumero(resultSet.getString("PoltronaNumero"));
                poltrona.setDisponivel(resultSet.getBoolean("Disponibilidade"));
                
                Sala sala = new Sala();
                sala.setId(resultSet.getInt("SalaId"));
                poltrona.setSala(sala);

                listaPoltronas.add(poltrona);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return listaPoltronas;
    }

    // Retorna o Objeto Poltrona em vez de Integer
    public Poltrona buscar(Sala sala, String numero) {
        String sql = "SELECT PoltronaId, Disponibilidade FROM Poltrona WHERE SalaId = ? AND PoltronaNumero = ?";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, sala.getId());
            preparedStatement.setString(2, numero);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Poltrona poltrona = new Poltrona();
                poltrona.setId(resultSet.getInt("PoltronaId"));
                poltrona.setNumero(numero);
                poltrona.setDisponivel(resultSet.getBoolean("Disponibilidade"));
                poltrona.setSala(sala);
                return poltrona;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void atualizarDisponibilidade(Poltrona poltrona) throws SQLException {
        String sql = "UPDATE Poltrona SET Disponibilidade = ? WHERE PoltronaId = ?";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setBoolean(1, poltrona.getDisponivel());
            preparedStatement.setInt(2, poltrona.getId());
            
            preparedStatement.executeUpdate();
        }
    }
}