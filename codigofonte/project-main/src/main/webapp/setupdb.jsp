<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="banco.DBConnection" %>
<%
    try (Connection conn = new DBConnection().getConnection();
         Statement stmt = conn.createStatement()) {
        
        try {
            stmt.execute("ALTER TABLE sala ADD COLUMN disponivel BOOL NOT NULL DEFAULT 1");
            out.println("Coluna disponivel adicionada.");
        } catch (Exception e) {
            out.println("Coluna disponivel ja existe ou erro.");
        }

        stmt.execute("CREATE TABLE IF NOT EXISTS poltrona (id INT PRIMARY KEY AUTO_INCREMENT, numero VARCHAR(10) NOT NULL, disponivel BOOL NOT NULL DEFAULT 1, sala_id INT, FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE)");
        out.println("Tabela poltrona criada.");

        stmt.execute("CREATE TABLE IF NOT EXISTS limpeza (id INT PRIMARY KEY AUTO_INCREMENT, data_limpeza DATETIME NOT NULL, status VARCHAR(100) NOT NULL, observacao TEXT, sala_id INT, FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE)");
        out.println("Tabela limpeza criada.");

        stmt.execute("CREATE TABLE IF NOT EXISTS manutencao (id INT PRIMARY KEY AUTO_INCREMENT, data_manutencao DATETIME NOT NULL, status VARCHAR(100) NOT NULL, observacao TEXT, sala_id INT, FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE)");
        out.println("Tabela manutencao criada.");

        stmt.execute("CREATE TABLE IF NOT EXISTS ingresso (id INT PRIMARY KEY AUTO_INCREMENT, status VARCHAR(100) NOT NULL, data_compra DATETIME NOT NULL, cliente_id INT, sessao_id INT, poltrona_id INT, FOREIGN KEY (cliente_id) REFERENCES cliente(id), FOREIGN KEY (sessao_id) REFERENCES sessao(id), FOREIGN KEY (poltrona_id) REFERENCES poltrona(id))");
        out.println("Tabela ingresso criada.");

        stmt.execute("DROP PROCEDURE IF EXISTS sp_ComprarIngresso");
        stmt.execute("CREATE PROCEDURE sp_ComprarIngresso(IN p_ClienteId INT, IN p_SessaoId INT, IN p_PoltronaId INT, IN p_Status VARCHAR(50), OUT p_IngressoId INT) BEGIN DECLARE v_DisponivelPoltrona BOOLEAN; DECLARE v_SalaId INT; DECLARE v_SalaDisponivel BOOLEAN; START TRANSACTION; SELECT sala_id INTO v_SalaId FROM sessao WHERE id = p_SessaoId; SELECT disponivel INTO v_SalaDisponivel FROM sala WHERE id = v_SalaId; SELECT disponivel INTO v_DisponivelPoltrona FROM poltrona WHERE id = p_PoltronaId FOR UPDATE; IF v_SalaDisponivel = 0 THEN ROLLBACK; SET p_IngressoId = -2; ELSEIF v_DisponivelPoltrona = 1 THEN UPDATE poltrona SET disponivel = 0 WHERE id = p_PoltronaId; INSERT INTO ingresso (status, data_compra, cliente_id, sessao_id, poltrona_id) VALUES (p_Status, NOW(), p_ClienteId, p_SessaoId, p_PoltronaId); SET p_IngressoId = LAST_INSERT_ID(); COMMIT; ELSE ROLLBACK; SET p_IngressoId = -1; END IF; END");

        stmt.execute("DROP PROCEDURE IF EXISTS sp_IniciarLimpeza");
        stmt.execute("CREATE PROCEDURE sp_IniciarLimpeza(IN p_SalaId INT) BEGIN UPDATE sala SET disponivel = 0 WHERE id = p_SalaId; INSERT INTO limpeza (data_limpeza, status, sala_id) VALUES (NOW(), 'EM ANDAMENTO', p_SalaId); END");

        stmt.execute("DROP PROCEDURE IF EXISTS sp_FinalizarLimpeza");
        stmt.execute("CREATE PROCEDURE sp_FinalizarLimpeza(IN p_SalaId INT) BEGIN UPDATE sala SET disponivel = 1 WHERE id = p_SalaId; UPDATE limpeza SET status = 'Concluído' WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO'; END");

        stmt.execute("DROP PROCEDURE IF EXISTS sp_IniciarManutencao");
        stmt.execute("CREATE PROCEDURE sp_IniciarManutencao(IN p_SalaId INT) BEGIN UPDATE sala SET disponivel = 0 WHERE id = p_SalaId; INSERT INTO manutencao (data_manutencao, status, sala_id) VALUES (NOW(), 'EM ANDAMENTO', p_SalaId); END");

        stmt.execute("DROP PROCEDURE IF EXISTS sp_FinalizarManutencao");
        stmt.execute("CREATE PROCEDURE sp_FinalizarManutencao(IN p_SalaId INT) BEGIN UPDATE sala SET disponivel = 1 WHERE id = p_SalaId; UPDATE manutencao SET status = 'Concluído' WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO'; END");
        
        try {
            stmt.execute("INSERT INTO poltrona (numero, disponivel, sala_id) VALUES ('A1', 1, 1), ('A2', 1, 1), ('A3', 1, 1), ('A4', 1, 1), ('A5', 1, 1), ('B1', 1, 1), ('B2', 1, 1), ('B3', 1, 1), ('B4', 1, 1), ('B5', 1, 1), ('C1', 1, 1), ('C2', 1, 1), ('C3', 1, 1), ('C4', 1, 1), ('C5', 1, 1), ('D1', 1, 1), ('D2', 1, 1), ('D3', 1, 1), ('D4', 1, 1), ('D5', 1, 1)");
            out.println("Poltronas inseridas.");
        } catch(Exception e) {
            out.println("Poltronas ja inseridas.");
        }
        
        try {
            stmt.execute("INSERT INTO poltrona (numero, disponivel, sala_id) VALUES ('A1', 1, 2), ('A2', 1, 2), ('A3', 1, 2), ('A4', 1, 2), ('A5', 1, 2)");
        } catch(Exception e) {}

        out.println("Setup concluido com sucesso!");
    } catch (Exception e) {
        e.printStackTrace(new java.io.PrintWriter(out));
    }
%>
