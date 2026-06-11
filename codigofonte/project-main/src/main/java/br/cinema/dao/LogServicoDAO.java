package br.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.cinema.model.ServicoLog;
import br.cinema.util.DBConnection;

public class LogServicoDAO {

    public List<ServicoLog> listarTodosLogs() {
        List<ServicoLog> logs = new ArrayList<>();
        
        // Consulta unificada que formata a data no MySQL e ordena cronologicamente de forma decrescente
        String sql = "SELECT Tipo, NumeroSala, DATE_FORMAT(RawData, '%d/%m/%Y %H:%i:%s') AS DataHora, Status FROM (" +
                     "    SELECT 'Limpeza' AS Tipo, s.NumeroSala, l.DataLimpeza AS RawData, l.StatusLimpeza AS Status " +
                     "    FROM Limpeza l JOIN Sala s ON l.SalaId = s.SalaId " +
                     "    UNION ALL " +
                     "    SELECT 'Manutencao' AS Tipo, s.NumeroSala, m.DataManutencao AS RawData, m.StatusManutencao AS Status " +
                     "    FROM Manutencao m JOIN Sala s ON m.SalaId = s.SalaId " +
                     ") AS tb_logs " +
                     "ORDER BY RawData DESC";

        try (Connection conexao = DBConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                ServicoLog log = new ServicoLog();
                log.setTipo(rs.getString("Tipo"));
                log.setNumeroSala(rs.getInt("NumeroSala"));
                log.setDataHora(rs.getString("DataHora"));
                log.setStatus(rs.getString("Status"));
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar logs: " + e.getMessage());
        }
        return logs;
    }
}