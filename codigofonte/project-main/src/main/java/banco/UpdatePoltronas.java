package banco;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UpdatePoltronas {
    public static void main(String[] args) {
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Deletando poltronas antigas...");
            stmt.execute("DELETE FROM poltrona");

            System.out.println("Buscando todas as salas...");
            List<Integer> salasIds = new ArrayList<>();
            ResultSet rs = stmt.executeQuery("SELECT id FROM sala");
            while (rs.next()) {
                salasIds.add(rs.getInt("id"));
            }

            String[] numeros = {
                "A1", "A2", "A3", "A4", "A5",
                "B1", "B2", "B3", "B4", "B5",
                "C1", "C2", "C3", "C4", "C5",
                "D1", "D2", "D3", "D4", "D5"
            };

            for (int salaId : salasIds) {
                System.out.println("Inserindo 20 poltronas na Sala " + salaId + "...");
                for (String num : numeros) {
                    stmt.execute("INSERT INTO poltrona (numero, disponivel, sala_id) VALUES ('" + num + "', 1, " + salaId + ")");
                }
            }

            System.out.println("Todas as salas agora possuem 20 poltronas.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
