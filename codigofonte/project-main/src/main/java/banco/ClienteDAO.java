package banco;

import java.sql.*;
import model.Cliente;

public class ClienteDAO {

    public Cliente buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, email, telefone, senha, cpf, validado, perfil FROM cliente WHERE email = ?";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(resultSet.getInt("id"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setEmail(resultSet.getString("email"));
                cliente.setTelefone(resultSet.getString("telefone"));
                cliente.setSenha(resultSet.getString("senha"));
                cliente.setCpf(resultSet.getString("cpf"));
                cliente.setValidado(resultSet.getBoolean("validado"));
                cliente.setPerfil(resultSet.getString("perfil"));
                return cliente;
            }
        }
        return null;
    }

    public boolean inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, email, telefone, senha, cpf, validado, perfil) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getEmail());
            preparedStatement.setString(3, cliente.getTelefone());
            preparedStatement.setString(4, cliente.getSenha());
            preparedStatement.setString(5, cliente.getCpf());
            preparedStatement.setBoolean(6, cliente.isValidado());
            preparedStatement.setString(7, cliente.getPerfil() != null ? cliente.getPerfil() : "USER");
            
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    public boolean validarEmail(String email) throws SQLException {
        String sql = "UPDATE cliente SET validado = TRUE WHERE email = ?";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, email);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    public boolean atualizarSenha(String email, String novaSenha) throws SQLException {
        String sql = "UPDATE cliente SET senha = ? WHERE email = ?";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, novaSenha);
            preparedStatement.setString(2, email);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }
}