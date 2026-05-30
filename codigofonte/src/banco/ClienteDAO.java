package banco;

import java.sql.*;
import model.Cliente;

public class ClienteDAO {

    // Retorna o próprio objeto Cliente com o ID preenchido
    public Cliente salvarOuBuscar(Cliente cliente) throws SQLException {
        Cliente clienteExistente = buscarPorEmail(cliente.getEmail());
        
        if (clienteExistente != null) {
            return clienteExistente;
        }
        return inserir(cliente);
    }

    // Retorna objeto Cliente em vez de Integer
    private Cliente buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT ClienteId, NomeCliente, Email, Telefone FROM Cliente WHERE Email = ?";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                Cliente clienteEncontrado = new Cliente();
                clienteEncontrado.setId(resultSet.getInt("ClienteId"));
                clienteEncontrado.setNome(resultSet.getString("NomeCliente"));
                clienteEncontrado.setEmail(resultSet.getString("Email"));
                clienteEncontrado.setTelefone(resultSet.getString("Telefone"));
                return clienteEncontrado;
            }
        }
        return null;
    }

    private Cliente inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (NomeCliente, Email, Telefone) VALUES (?, ?, ?)";
        
        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            preparedStatement.setString(1, cliente.getNome());
            preparedStatement.setString(2, cliente.getEmail());
            preparedStatement.setString(3, cliente.getTelefone());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                // Atualiza o ID no próprio objeto e o retorna
                cliente.setId(resultSet.getInt(1));
                return cliente;
            }
        }
        throw new SQLException("Erro ao inserir cliente.");
    }
}