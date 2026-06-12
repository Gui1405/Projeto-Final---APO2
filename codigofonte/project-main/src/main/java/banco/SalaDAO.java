package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Sala;

public class SalaDAO {

    public Sala buscarPorId(Sala salaFiltro) throws SQLException {
        String sql = "SELECT * FROM sala WHERE id = ?";
        Sala salaRetorno = null;

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, salaFiltro.getId());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    salaRetorno = new Sala();
                    salaRetorno.setId(resultSet.getInt("id"));
                    // Web DDL.sql "nome" represents NumeroSala (e.g., "Sala 1")
                    try {
                        String nomeStr = resultSet.getString("nome");
                        if(nomeStr != null && nomeStr.toLowerCase().contains("sala")) {
                            salaRetorno.setNumero(Integer.parseInt(nomeStr.replaceAll("[^0-9]", "")));
                        } else {
                            salaRetorno.setNumero(resultSet.getInt("nome"));
                        }
                    } catch(Exception e) {
                        salaRetorno.setNumero(resultSet.getInt("id"));
                    }
                    salaRetorno.setCapacidade(resultSet.getInt("capacidade"));
                    salaRetorno.setDisponivel(resultSet.getBoolean("disponivel"));
                }
            }
        }
        return salaRetorno;
    }

    public java.util.List<Sala> listarTodas() throws SQLException {
        String sql = "SELECT * FROM sala";
        java.util.List<Sala> salas = new java.util.ArrayList<>();

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Sala sala = new Sala();
                sala.setId(resultSet.getInt("id"));
                try {
                    String nomeStr = resultSet.getString("nome");
                    if(nomeStr != null && nomeStr.toLowerCase().contains("sala")) {
                        sala.setNumero(Integer.parseInt(nomeStr.replaceAll("[^0-9]", "")));
                    } else {
                        sala.setNumero(resultSet.getInt("nome"));
                    }
                } catch(Exception e) {
                    sala.setNumero(resultSet.getInt("id"));
                }
                sala.setCapacidade(resultSet.getInt("capacidade"));
                sala.setDisponivel(resultSet.getBoolean("disponivel"));
                salas.add(sala);
            }
        }
        return salas;
    }
}