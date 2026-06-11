package br.cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import br.cinema.model.Cliente;
import br.cinema.util.DBConnection;

public class ClienteDAO {

    public boolean cadastrar(Cliente cliente) {
        String sql = "INSERT INTO Cliente (NomeCliente, Email, Telefone, Senha, Perfil) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conexao = DBConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
             
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getSenha());
            stmt.setString(5, "CLIENTE"); // Novo cadastro sempre será cliente comum
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cliente autenticar(String email, String senha) {
        String sql = "SELECT * FROM Cliente WHERE Email = ? AND Senha = ?";
        
        try (Connection conexao = DBConnection.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
             
            stmt.setString(1, email);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("ClienteId"));
                    cliente.setNome(rs.getString("NomeCliente"));
                    cliente.setEmail(rs.getString("Email"));
                    cliente.setTelefone(rs.getString("Telefone"));
                    cliente.setPerfil(rs.getString("Perfil"));
                    // Não populamos a senha no objeto por segurança
                    return cliente;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null se não encontrar o usuário ou a senha estiver incorreta
    }
}