package banco;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckDB {
    public static void main(String[] args) {
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Checking poltrona table:");
            try {
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM poltrona WHERE sala_id = 1");
                if (rs.next()) {
                    System.out.println("Poltronas in sala 1: " + rs.getInt(1));
                }
            } catch(Exception e) {
                System.out.println("Erro poltrona: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
