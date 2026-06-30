-- Script de Criacao DDL
DROP DATABASE IF EXISTS cinema;
CREATE DATABASE IF NOT EXISTS cinema;
USE cinema;

CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    validado BOOLEAN DEFAULT FALSE,
    perfil ENUM('USER', 'ADMIN') DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS filme (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    duracao INT NOT NULL,
    genero VARCHAR(50) NOT NULL,
    classificacao VARCHAR(10) NOT NULL,
    sinopse TEXT
);

CREATE TABLE IF NOT EXISTS sala (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    capacidade INT NOT NULL,
    disponivel BOOL NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS sessao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    filme_id INT NOT NULL,
    sala_id INT NOT NULL,
    horario DATETIME NOT NULL,
    valor_ingresso DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (filme_id) REFERENCES filme(id) ON DELETE CASCADE,
    FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reserva (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    sessao_id INT NOT NULL,
    quantidade_ingressos INT NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    data_reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (sessao_id) REFERENCES sessao(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS poltrona (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(10) NOT NULL,
    disponivel BOOL NOT NULL DEFAULT 1,
    sala_id INT,
    FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS limpeza (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_limpeza DATETIME NOT NULL,
    status VARCHAR(100) NOT NULL,
    observacao TEXT,
    sala_id INT,
    FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS manutencao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_manutencao DATETIME NOT NULL,
    status VARCHAR(100) NOT NULL,
    observacao TEXT,
    sala_id INT,
    FOREIGN KEY (sala_id) REFERENCES sala(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ingresso (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(100) NOT NULL,
    data_compra DATETIME NOT NULL,
    cliente_id INT,
    sessao_id INT,
    poltrona_id INT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (sessao_id) REFERENCES sessao(id),
    FOREIGN KEY (poltrona_id) REFERENCES poltrona(id)
);

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_ComprarIngresso $$
CREATE PROCEDURE sp_ComprarIngresso(
    IN p_ClienteId INT,
    IN p_SessaoId INT,
    IN p_PoltronaId INT,
    IN p_Status VARCHAR(50),
    OUT p_IngressoId INT
)
BEGIN
    DECLARE v_DisponivelPoltrona BOOLEAN;
    DECLARE v_SalaId INT;
    DECLARE v_SalaDisponivel BOOLEAN;
    START TRANSACTION;
    SELECT sala_id INTO v_SalaId FROM sessao WHERE id = p_SessaoId;
    SELECT disponivel INTO v_SalaDisponivel FROM sala WHERE id = v_SalaId;
    SELECT disponivel INTO v_DisponivelPoltrona FROM poltrona WHERE id = p_PoltronaId FOR UPDATE;
    IF v_SalaDisponivel = 0 THEN
        ROLLBACK;
        SET p_IngressoId = -2;
    ELSEIF v_DisponivelPoltrona = 1 THEN
        UPDATE poltrona SET disponivel = 0 WHERE id = p_PoltronaId;
        INSERT INTO ingresso (status, data_compra, cliente_id, sessao_id, poltrona_id)
        VALUES (p_Status, NOW(), p_ClienteId, p_SessaoId, p_PoltronaId);
        SET p_IngressoId = LAST_INSERT_ID();
        COMMIT;
    ELSE
        ROLLBACK;
        SET p_IngressoId = -1;
    END IF;
END $$

DROP PROCEDURE IF EXISTS sp_IniciarLimpeza $$
CREATE PROCEDURE sp_IniciarLimpeza(IN p_SalaId INT)
BEGIN
    UPDATE sala SET disponivel = 0 WHERE id = p_SalaId;
    INSERT INTO limpeza (data_limpeza, status, sala_id) VALUES (NOW(), 'EM ANDAMENTO', p_SalaId);
END $$

DROP PROCEDURE IF EXISTS sp_FinalizarLimpeza $$
CREATE PROCEDURE sp_FinalizarLimpeza(IN p_SalaId INT)
BEGIN
    UPDATE limpeza SET status = 'Concluído' WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO';
    
    IF NOT EXISTS (SELECT 1 FROM manutencao WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO') THEN
        UPDATE sala SET disponivel = 1 WHERE id = p_SalaId;
    END IF;
END $$

DROP PROCEDURE IF EXISTS sp_IniciarManutencao $$
CREATE PROCEDURE sp_IniciarManutencao(IN p_SalaId INT)
BEGIN
    UPDATE sala SET disponivel = 0 WHERE id = p_SalaId;
    INSERT INTO manutencao (data_manutencao, status, sala_id) VALUES (NOW(), 'EM ANDAMENTO', p_SalaId);
END $$

DROP PROCEDURE IF EXISTS sp_FinalizarManutencao $$
CREATE PROCEDURE sp_FinalizarManutencao(IN p_SalaId INT)
BEGIN
    UPDATE manutencao SET status = 'Concluído' WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO';
    
    IF NOT EXISTS (SELECT 1 FROM limpeza WHERE sala_id = p_SalaId AND status = 'EM ANDAMENTO') THEN
        UPDATE sala SET disponivel = 1 WHERE id = p_SalaId;
    END IF;
END $$

DROP PROCEDURE IF EXISTS sp_CancelarReserva $$
CREATE PROCEDURE sp_CancelarReserva(
    IN p_IngressoId INT,
    OUT p_Mensagem VARCHAR(100)
)
BEGIN
    DECLARE v_Status VARCHAR(100);
    DECLARE v_PoltronaId INT;
    
    START TRANSACTION;
    
    SELECT status, poltrona_id INTO v_Status, v_PoltronaId FROM ingresso WHERE id = p_IngressoId FOR UPDATE;
    
    IF v_Status IS NULL THEN
        SET p_Mensagem = 'Ingresso não encontrado';
        ROLLBACK;
    ELSEIF v_Status = 'REEMBOLSADO' THEN
        SET p_Mensagem = 'Ingresso já estava reembolsado';
        ROLLBACK;
    ELSE
        UPDATE ingresso SET status = 'REEMBOLSADO' WHERE id = p_IngressoId;
        UPDATE poltrona SET disponivel = 1 WHERE id = v_PoltronaId;
        SET p_Mensagem = 'Sucesso';
        COMMIT;
    END IF;
END $$

DELIMITER ;
