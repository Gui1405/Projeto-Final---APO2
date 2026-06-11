package br.cinema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // URL de conexão padrão para MySQL 8.0+
    private static final String URL = "jdbc:mysql://localhost:3306/cinema?useTimezone=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "1234";

    public static Connection getConnection() throws SQLException {
        try {
            // Registra o driver do MySQL explicitamente (necessário em aplicações Web)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver do MySQL não encontrado.", e);
        }
    }
}