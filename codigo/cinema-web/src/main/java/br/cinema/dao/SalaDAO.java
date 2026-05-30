package br.cinema.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.cinema.model.Sala;
import br.cinema.util.DBConnection;

public class SalaDAO {

    public List<Sala> listarTodas() {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM Sala ORDER BY NumeroSala ASC";
        
        try (Connection conexao = DBConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Sala s = new Sala();
                s.setId(rs.getInt("SalaId"));
                s.setNumeroSala(rs.getInt("NumeroSala"));
                s.setCapacidade(rs.getInt("Capacidade"));
                s.setTipoSala(rs.getString("TipoSala"));
                s.setDisponivel(rs.getBoolean("Disponivel"));
                salas.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salas;
    }

    public boolean executarServicoSala(int salaId, String acao) {
        String procedureQuery = "";
        
        switch (acao) {
            case "INICIAR_LIMPEZA": procedureQuery = "{CALL sp_IniciarLimpeza(?)}"; break;
            case "FINALIZAR_LIMPEZA": procedureQuery = "{CALL sp_FinalizarLimpeza(?)}"; break;
            case "INICIAR_MANUTENCAO": procedureQuery = "{CALL sp_IniciarManutencao(?)}"; break;
            case "FINALIZAR_MANUTENCAO": procedureQuery = "{CALL sp_FinalizarManutencao(?)}"; break;
            default: return false;
        }

        try (Connection conexao = DBConnection.getConnection();
             CallableStatement stmt = conexao.prepareCall(procedureQuery)) {
             
            stmt.setInt(1, salaId);
            stmt.execute();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}