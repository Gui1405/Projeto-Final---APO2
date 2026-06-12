package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Manutencao;
import model.Sala;

public class ManutencaoDAO {

    public void registrarInicio(Manutencao manutencao) throws SQLException {
        String sql = "{call sp_IniciarManutencao(?)}";
        
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Extrai ID do objeto Sala
            ps.setInt(1, manutencao.getSala().getId());
            ps.execute();
        }
    }

    public void registrarFim(Manutencao manutencao) throws SQLException {
        String sql = "{call sp_FinalizarManutencao(?)}";
        
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, manutencao.getSala().getId());
            ps.execute();
        }
    }
    
    // Recebe Objeto Sala
    public String buscarUltimoStatus(Sala sala) {
        String status = null;
        String sql = "SELECT status FROM manutencao WHERE sala_id = ? ORDER BY data_manutencao DESC LIMIT 1";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, sala.getId());
            try (java.sql.ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    status = resultSet.getString("status");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return status;
    }

    public String buscarHistorico(Sala sala) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM manutencao WHERE sala_id = ? ORDER BY data_manutencao DESC";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sala.getId());
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("Início: ").append(rs.getObject("data_manutencao"))
                      .append(" | Status: ").append(rs.getString("status"))
                      .append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao buscar histórico.";
        }
        return sb.length() > 0 ? sb.toString() : "Nenhum registro encontrado.";
    }
}