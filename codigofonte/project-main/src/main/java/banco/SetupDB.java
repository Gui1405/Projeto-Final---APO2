package banco;

import java.sql.Connection;
import java.sql.Statement;

public class SetupDB {
    public static void main(String[] args) {
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Adicionando coluna disponivel na sala...");
            try {
                stmt.execute("ALTER TABLE sala ADD COLUMN disponivel BOOL NOT NULL DEFAULT 1");
            } catch (Exception e) {
                System.out.println("Coluna disponivel ja existe ou erro: " + e.getMessage());
            }

            System.out.println("Criando tabela poltrona...");
            stmt.execute("CREATE TABLE IF NOT EXISTS poltrona (" +
                         "id INT PRIMARY KEY AUTO_INCREMENT, " +
                         "numero VARCHAR(10) NOT NULL, " +
                         "disponivel BOOL NOT NULL DEFAULT 1, " +
                         "sala_id INT, " +
                         "FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE" +
                         ")");

            System.out.println("Criando tabela limpeza...");
            stmt.execute("CREATE TABLE IF NOT EXISTS limpeza (" +
                         "id INT PRIMARY KEY AUTO_INCREMENT, " +
                         "data_limpeza DATETIME NOT NULL, " +
                         "status VARCHAR(100) NOT NULL, " +
                         "observacao TEXT, " +
                         "sala_id INT, " +
                         "FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE" +
                         ")");

            System.out.println("Criando tabela manutencao...");
            stmt.execute("CREATE TABLE IF NOT EXISTS manutencao (" +
                         "id INT PRIMARY KEY AUTO_INCREMENT, " +
                         "data_manutencao DATETIME NOT NULL, " +
                         "status VARCHAR(100) NOT NULL, " +
                         "observacao TEXT, " +
                         "sala_id INT, " +
                         "FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE" +
                         ")");

            System.out.println("Criando tabela ingresso...");
            stmt.execute("CREATE TABLE IF NOT EXISTS ingresso (" +
                         "id INT PRIMARY KEY AUTO_INCREMENT, " +
                         "status VARCHAR(100) NOT NULL, " +
                         "data_compra DATETIME NOT NULL, " +
                         "cliente_id INT, " +
                         "sessao_id INT, " +
                         "poltrona_id INT, " +
                         "FOREIGN KEY (cliente_id) REFERENCES cliente(id), " +
                         "FOREIGN KEY (sessao_id) REFERENCES sessao(id), " +
                         "FOREIGN KEY (poltrona_id) REFERENCES poltrona(id)" +
                         ")");

            System.out.println("Criando Stored Procedures...");
            stmt.execute("DROP PROCEDURE IF EXISTS sp_ComprarIngresso");
            stmt.execute("CREATE PROCEDURE sp_ComprarIngresso(" +
                         "    IN p_ClienteId INT, " +
                         "    IN p_SessaoId INT, " +
                         "    IN p_PoltronaId INT, " +
                         "    IN p_Status VARCHAR(50), " +
                         "    OUT p_IngressoId INT" +
                         ") " +
                         "BEGIN " +
                         "    DECLARE v_DisponivelPoltrona BOOLEAN; " +
                         "    DECLARE v_SalaId INT; " +
                         "    DECLARE v_SalaDisponivel BOOLEAN; " +
                         "    START TRANSACTION; " +
                         "    SELECT sala_id INTO v_SalaId FROM sessao WHERE id = p_SessaoId; " +
                         "    SELECT disponivel INTO v_SalaDisponivel FROM sala WHERE id = v_SalaId; " +
                         "    SELECT disponivel INTO v_DisponivelPoltrona FROM poltrona WHERE id = p_PoltronaId FOR UPDATE; " +
                         "    IF v_SalaDisponivel = 0 THEN " +
                         "        ROLLBACK; " +
                         "        SET p_IngressoId = -2; " +
                         "    ELSEIF v_DisponivelPoltrona = 1 THEN " +
                         "        UPDATE poltrona SET disponivel = 0 WHERE id = p_PoltronaId; " +
                         "        INSERT INTO ingresso (status, data_compra, cliente_id, sessao_id, poltrona_id) " +
                         "        VALUES (p_Status, NOW(), p_ClienteId, p_SessaoId, p_PoltronaId); " +
                         "        SET p_IngressoId = LAST_INSERT_ID(); " +
                         "        COMMIT; " +
                         "    ELSE " +
                         "        ROLLBACK; " +
                         "        SET p_IngressoId = -1; " +
                         "    END IF; " +
                         "END");

            stmt.execute("DROP PROCEDURE IF EXISTS sp_IniciarLimpeza");
            stmt.execute("CREATE PROCEDURE sp_IniciarLimpeza(IN p_SalaId INT) " +
                         "BEGIN " +
                         "    UPDATE sala SET disponivel = 0 WHERE id = p_SalaId; " +
                         "    INSERT INTO limpeza (data_limpeza, status, sala_id) VALUES (NOW(), 'EM ANDAMENTO', p_SalaId); " +
                         "END");

            stmt.execute("DROP PROCEDURE IF EXISTS sp_FinalizarLimpeza");
            stmt.execute("CREATE PROCEDURE sp_FinalizarLimpeza(IN p_SalaId INT) " +
                         "BEGIN " +
                         "    UPDATE limpeza SET status = 'Concluído' WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO'; " +
                         "    IF NOT EXISTS (SELECT 1 FROM manutencao WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO') THEN " +
                         "        UPDATE sala SET disponivel = 1 WHERE id = p_SalaId; " +
                         "    END IF; " +
                         "END");

            stmt.execute("DROP PROCEDURE IF EXISTS sp_IniciarManutencao");
            stmt.execute("CREATE PROCEDURE sp_IniciarManutencao(IN p_SalaId INT) " +
                         "BEGIN " +
                         "    UPDATE sala SET disponivel = 0 WHERE id = p_SalaId; " +
                         "    INSERT INTO manutencao (data_manutencao, status, sala_id) VALUES (NOW(), 'EM ANDAMENTO', p_SalaId); " +
                         "END");

            stmt.execute("DROP PROCEDURE IF EXISTS sp_FinalizarManutencao");
            stmt.execute("CREATE PROCEDURE sp_FinalizarManutencao(IN p_SalaId INT) " +
                         "BEGIN " +
                         "    UPDATE manutencao SET status = 'Concluído' WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO'; " +
                         "    IF NOT EXISTS (SELECT 1 FROM limpeza WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO') THEN " +
                         "        UPDATE sala SET disponivel = 1 WHERE id = p_SalaId; " +
                         "    END IF; " +
                         "END");

            stmt.execute("DROP PROCEDURE IF EXISTS sp_CancelarReserva");
            stmt.execute("CREATE PROCEDURE sp_CancelarReserva(IN p_IngressoId INT, OUT p_Mensagem VARCHAR(100)) " +
                         "BEGIN " +
                         "    DECLARE v_Status VARCHAR(100); " +
                         "    DECLARE v_PoltronaId INT; " +
                         "    START TRANSACTION; " +
                         "    SELECT status, poltrona_id INTO v_Status, v_PoltronaId FROM ingresso WHERE id = p_IngressoId FOR UPDATE; " +
                         "    IF v_Status IS NULL THEN " +
                         "        SET p_Mensagem = 'Ingresso não encontrado'; " +
                         "        ROLLBACK; " +
                         "    ELSEIF v_Status = 'REEMBOLSADO' THEN " +
                         "        SET p_Mensagem = 'Ingresso já estava reembolsado'; " +
                         "        ROLLBACK; " +
                         "    ELSE " +
                         "        UPDATE ingresso SET status = 'REEMBOLSADO' WHERE id = p_IngressoId; " +
                         "        UPDATE poltrona SET disponivel = 1 WHERE id = v_PoltronaId; " +
                         "        SET p_Mensagem = 'Sucesso'; " +
                         "        COMMIT; " +
                         "    END IF; " +
                         "END");
            
            System.out.println("Inserindo dados iniciais (Sala, Filme, Sessao)...");
            try {
                stmt.execute("INSERT INTO sala (id, nome, capacidade, disponivel) VALUES (1, 'Sala 1 - IMAX', 20, 1), (2, 'Sala 2 - VIP', 20, 1), (3, 'Sala 3 - Standard', 20, 1) ON DUPLICATE KEY UPDATE nome=VALUES(nome), capacidade=VALUES(capacidade), disponivel=VALUES(disponivel)");
                stmt.execute("INSERT INTO filme (id, titulo, duracao, genero, classificacao, sinopse) VALUES (1, 'Matrix (Remastered)', 136, 'Ficção Científica', '14 anos', 'Um programador descobre que o mundo em que vive é uma simulação criada por máquinas e se junta à resistência.'), (2, 'O Auto da Compadecida 2', 120, 'Comédia', '12 anos', 'João Grilo e Chicó retornam para novas aventuras no sertão de Taperoá, cheios de histórias e confusões.'), (3, 'Coringa: Delírio a Dois', 138, 'Drama / Musical', '16 anos', 'Arthur Fleck encontra o amor verdadeiro em Arkham e a música que sempre esteve dentro dele.') ON DUPLICATE KEY UPDATE titulo=VALUES(titulo), duracao=VALUES(duracao), genero=VALUES(genero), classificacao=VALUES(classificacao), sinopse=VALUES(sinopse)");
                stmt.execute("INSERT INTO sessao (id, filme_id, sala_id, horario, valor_ingresso) VALUES (1, 1, 1, DATE_ADD(NOW(), INTERVAL 2 HOUR), 45.00), (2, 2, 3, DATE_ADD(NOW(), INTERVAL 4 HOUR), 25.00), (3, 3, 2, DATE_ADD(NOW(), INTERVAL 5 HOUR), 60.00) ON DUPLICATE KEY UPDATE filme_id=VALUES(filme_id), sala_id=VALUES(sala_id), horario=VALUES(horario), valor_ingresso=VALUES(valor_ingresso)");
            } catch(Exception e) {
                System.out.println("Dados iniciais ja inseridos ou erro: " + e.getMessage());
            }

            System.out.println("Inserindo poltronas para as Salas...");
            try {
                stmt.execute("INSERT INTO poltrona (numero, disponivel, sala_id) VALUES " +
                             "('A1', 1, 1), ('A2', 1, 1), ('A3', 1, 1), ('A4', 1, 1), ('A5', 1, 1), " +
                             "('B1', 1, 1), ('B2', 1, 1), ('B3', 1, 1), ('B4', 1, 1), ('B5', 1, 1), " +
                             "('C1', 1, 1), ('C2', 1, 1), ('C3', 1, 1), ('C4', 1, 1), ('C5', 1, 1), " +
                             "('D1', 1, 1), ('D2', 1, 1), ('D3', 1, 1), ('D4', 1, 1), ('D5', 1, 1), " +
                             "('A1', 1, 2), ('A2', 1, 2), ('A3', 1, 2), ('A4', 1, 2), ('A5', 1, 2), " +
                             "('B1', 1, 2), ('B2', 1, 2), ('B3', 1, 2), ('B4', 1, 2), ('B5', 1, 2), " +
                             "('C1', 1, 2), ('C2', 1, 2), ('C3', 1, 2), ('C4', 1, 2), ('C5', 1, 2), " +
                             "('D1', 1, 2), ('D2', 1, 2), ('D3', 1, 2), ('D4', 1, 2), ('D5', 1, 2), " +
                             "('A1', 1, 3), ('A2', 1, 3), ('A3', 1, 3), ('A4', 1, 3), ('A5', 1, 3), " +
                             "('B1', 1, 3), ('B2', 1, 3), ('B3', 1, 3), ('B4', 1, 3), ('B5', 1, 3), " +
                             "('C1', 1, 3), ('C2', 1, 3), ('C3', 1, 3), ('C4', 1, 3), ('C5', 1, 3), " +
                             "('D1', 1, 3), ('D2', 1, 3), ('D3', 1, 3), ('D4', 1, 3), ('D5', 1, 3)");
            } catch(Exception e) {
                System.out.println("Poltronas ja inseridas ou erro: " + e.getMessage());
            }

            System.out.println("Banco atualizado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
