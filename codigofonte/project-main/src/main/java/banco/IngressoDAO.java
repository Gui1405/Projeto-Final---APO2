package banco;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import model.Ingresso;
import model.Reserva;
import model.Poltrona;

public class IngressoDAO {

    // Retorna o objeto Ingresso com o ID gerado
    public Ingresso inserir(Ingresso ingresso) throws SQLException {
        String sql = "{call sp_ComprarIngresso(?, ?, ?, ?, ?)}";

        try (Connection connection = new DBConnection().getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {
            
            callableStatement.setInt(1, ingresso.getCliente().getId());
            callableStatement.setInt(2, ingresso.getSessao().getId());
            callableStatement.setInt(3, ingresso.getPoltrona().getId());
            callableStatement.setString(4, ingresso.getStatus()); 
            
            callableStatement.registerOutParameter(5, Types.INTEGER);
            
            callableStatement.execute();
            
            int idGerado = callableStatement.getInt(5);
            ingresso.setId(idGerado);
        }
        return ingresso;
    }

    public Reserva buscarPorIngresso(Ingresso filtroIngresso) {
        String sql = "SELECT IngressoId, StatusIngresso, PoltronaId FROM Ingresso WHERE IngressoId = ?";
        Reserva reserva = null;

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, filtroIngresso.getId());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Poltrona poltrona = new Poltrona();
                    poltrona.setId(resultSet.getInt("PoltronaId"));

                    reserva = new Reserva(
                        resultSet.getInt("IngressoId"),
                        resultSet.getString("StatusIngresso"),
                        poltrona
                    );
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return reserva;
    }

    public String cancelar(Reserva reserva) throws SQLException {
        String sql = "{call sp_CancelarReserva(?, ?)}"; 
        String resultado = "Erro";

        try (Connection connection = new DBConnection().getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {
            
            callableStatement.setInt(1, reserva.getId());
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            
            callableStatement.execute();
            
            resultado = callableStatement.getString(2);
        }
        return resultado;
    }
}