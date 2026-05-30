DROP DATABASE IF EXISTS cinema;
CREATE DATABASE cinema;
USE cinema;

-- =============================================
-- 1. CRIAÇÃO DAS TABELAS
-- =============================================

CREATE TABLE Filme (
    FilmeId INT PRIMARY KEY AUTO_INCREMENT,
    Titulo TEXT(255) NOT NULL,
    Duracao INT NOT NULL,
    Genero TEXT(255) NOT NULL,
    Classificacao TEXT(100) NOT NULL,
    Sinopse LONGTEXT NOT NULL
);

CREATE TABLE Sala (
    SalaId INT PRIMARY KEY AUTO_INCREMENT,
    NumeroSala INT NOT NULL,
    Capacidade INT NOT NULL,
    TipoSala TEXT(100) NOT NULL,
    Disponivel BOOL NOT NULL DEFAULT 1 -- 1 = Disponível, 0 = Indisponível (Manutenção/Limpeza)
);

CREATE TABLE Sessao (
    SessaoId INT PRIMARY KEY AUTO_INCREMENT,
    HorarioInicio DATETIME NOT NULL,
    HorarioFim DATETIME NOT NULL,
    FilmeId INT,
    SalaId INT,
    FOREIGN KEY (FilmeId) REFERENCES Filme(FilmeId),
    FOREIGN KEY (SalaId) REFERENCES Sala(SalaId)
);

CREATE TABLE Limpeza (
    LimpezaId INT PRIMARY KEY AUTO_INCREMENT,
    DataLimpeza DATETIME NOT NULL,
    StatusLimpeza TEXT(100) NOT NULL,
    ObservacaoLimpeza LONGTEXT,
    SalaId INT,
    FOREIGN KEY (SalaId) REFERENCES Sala(SalaId)
);

CREATE TABLE Manutencao (
    ManutencaoId INT PRIMARY KEY AUTO_INCREMENT,
    DataManutencao DATETIME NOT NULL,
    StatusManutencao TEXT(100) NOT NULL,
    ObservacaoManutencao LONGTEXT,
    SalaId INT,
    FOREIGN KEY (SalaId) REFERENCES Sala(SalaId)
);

CREATE TABLE ControladorDeAcesso (
    ContAcessId INT PRIMARY KEY AUTO_INCREMENT,
    ContAcessNome TEXT(255) NOT NULL
);

CREATE TABLE ControladorDeSala (
    ContSalaId INT PRIMARY KEY AUTO_INCREMENT,
    ContSalaNome TEXT(255) NOT NULL,
    SalaId INT,
    FOREIGN KEY (SalaId) REFERENCES Sala(SalaId)
);

CREATE TABLE Poltrona (
    PoltronaId INT PRIMARY KEY AUTO_INCREMENT,
    PoltronaNumero TEXT(100) NOT NULL,
    Disponibilidade BOOL NOT NULL DEFAULT 1,
    SalaId INT,
    FOREIGN KEY (SalaId) REFERENCES Sala(SalaId)
);

CREATE TABLE Bilheteria (
    BilheteriaId INT PRIMARY KEY AUTO_INCREMENT,
    BilheteriaNome TEXT(255) NOT NULL
);

CREATE TABLE Cliente (
    ClienteId INT PRIMARY KEY AUTO_INCREMENT,
    NomeCliente TEXT(255) NOT NULL,
    Email TEXT NOT NULL,
    Telefone TEXT NOT NULL
);

CREATE TABLE Ingresso (
    IngressoId INT PRIMARY KEY AUTO_INCREMENT,
    StatusIngresso TEXT(100) NOT NULL, -- [CORREÇÃO]: Removido duplicidade no nome da coluna
    DataCompra DATETIME NOT NULL,
    ClienteId INT,
    SessaoId INT,
    PoltronaId INT,
    FOREIGN KEY (ClienteId) REFERENCES Cliente(ClienteId),
    FOREIGN KEY (SessaoId) REFERENCES Sessao(SessaoId),
    FOREIGN KEY (PoltronaId) REFERENCES Poltrona(PoltronaId)
);

-- =============================================
-- 2. INSERÇÃO DE DADOS (CARGA INICIAL)
-- =============================================

INSERT INTO Filme (Titulo, Duracao, Genero, Classificacao, Sinopse) VALUES
('Vingadores: Ultimato', 181, 'Ação', '14 anos', 'Os heróis restantes lutam para reverter o estalar de dedos de Thanos.'),
('Interestelar', 169, 'Ficção Científica', '10 anos', 'Um grupo viaja pelo espaço em busca de um novo lar para a humanidade.'),
('Toy Story 4', 100, 'Animação', 'Livre', 'Woody e Buzz vivem novas aventuras com um novo brinquedo.'),
('O Exorcista', 122, 'Terror', '18 anos', 'Uma jovem é possuída e dois padres tentam salvá-la.');

INSERT INTO Sala (NumeroSala, Capacidade, TipoSala, Disponivel) VALUES
(1, 100, '2D', 1),
(2, 150, '3D', 1),
(3, 80, 'VIP', 1);

INSERT INTO ControladorDeAcesso (ContAcessNome) VALUES
('João Menezes'), ('Carla Santos'), ('Rafael Souza');

INSERT INTO ControladorDeSala (ContSalaNome, SalaId) VALUES
('Pedro Almeida', 1), ('Ana Paula', 2), ('Marcos Lima', 3);

-- Inserção simplificada de poltronas
INSERT INTO Poltrona (PoltronaNumero, Disponibilidade, SalaId) VALUES
-- SALA 1
('A1', 1, 1), ('A2', 1, 1), ('A3', 1, 1), ('A4', 1, 1), ('A5', 1, 1),
('B1', 1, 1), ('B2', 1, 1), ('B3', 1, 1), ('B4', 1, 1), ('B5', 1, 1),
('C1', 1, 1), ('C2', 1, 1), ('C3', 1, 1), ('C4', 1, 1), ('C5', 1, 1),
('D1', 1, 1), ('D2', 1, 1), ('D3', 1, 1), ('D4', 1, 1), ('D5', 1, 1),

-- SALA 2
('A1', 1, 2), ('A2', 1, 2), ('A3', 1, 2), ('A4', 1, 2), ('A5', 1, 2),
('B1', 1, 2), ('B2', 1, 2), ('B3', 1, 2), ('B4', 1, 2), ('B5', 1, 2),
('C1', 1, 2), ('C2', 1, 2), ('C3', 1, 2), ('C4', 1, 2), ('C5', 1, 2),
('D1', 1, 2), ('D2', 1, 2), ('D3', 1, 2), ('D4', 1, 2), ('D5', 1, 2),

-- SALA 3
('A1', 1, 3), ('A2', 1, 3), ('A3', 1, 3), ('A4', 1, 3), ('A5', 1, 3),
('B1', 1, 3), ('B2', 1, 3), ('B3', 1, 3), ('B4', 1, 3), ('B5', 1, 3),
('C1', 1, 3), ('C2', 1, 3), ('C3', 1, 3), ('C4', 1, 3), ('C5', 1, 3),
('D1', 1, 3), ('D2', 1, 3), ('D3', 1, 3), ('D4', 1, 3), ('D5', 1, 3);

INSERT INTO Bilheteria (BilheteriaNome) VALUES
('Bilheteria Principal'), ('Bilheteria VIP');

INSERT INTO Cliente (NomeCliente, Email, Telefone) VALUES
('Arthur Ferreira', 'arthur@email.com', '11999990000');

INSERT INTO Sessao (HorarioInicio, HorarioFim, FilmeId, SalaId) VALUES
('2025-12-01 14:00:00', '2025-12-01 17:00:00', 1, 1) , ('2025-12-01 14:00:00', '2025-12-01 17:00:00', 2, 2) , ('2025-12-01 14:00:00', '2025-12-01 17:00:00', 3, 3) , ('2025-12-01 17:00:00', '2025-12-01 18:00:00', 4, 1);

-- =============================================
-- 3. STORED PROCEDURES (LÓGICA AUTOMÁTICA)
-- =============================================

DELIMITER $$

-- ---------------------------------------------------------
-- PROCEDURE: Comprar Ingresso
-- [ATUALIZADO]: Agora verifica se a Sala está disponível (sem manutenção/limpeza)
-- ---------------------------------------------------------
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

    -- 1. Descobre a Sala da Sessão
    SELECT SalaId INTO v_SalaId FROM Sessao WHERE SessaoId = p_SessaoId;

    -- 2. Verifica se a Sala está Aberta (Disponivel = 1)
    SELECT Disponivel INTO v_SalaDisponivel FROM Sala WHERE SalaId = v_SalaId;

    -- 3. Verifica se a Poltrona está livre
    SELECT Disponibilidade INTO v_DisponivelPoltrona 
    FROM Poltrona WHERE PoltronaId = p_PoltronaId FOR UPDATE;

    -- LÓGICA DE VALIDAÇÃO
    IF v_SalaDisponivel = 0 THEN
        -- Sala em manutenção/limpeza
        ROLLBACK;
        SET p_IngressoId = -2; -- Código de erro para "Sala Indisponível"
    ELSEIF v_DisponivelPoltrona = 1 THEN
        -- Tudo certo, realiza a compra
        UPDATE Poltrona SET Disponibilidade = 0 WHERE PoltronaId = p_PoltronaId;
        INSERT INTO Ingresso (StatusIngresso, DataCompra, ClienteId, SessaoId, PoltronaId)
        VALUES (p_Status, NOW(), p_ClienteId, p_SessaoId, p_PoltronaId);
        SET p_IngressoId = LAST_INSERT_ID();
        COMMIT;
    ELSE
        -- Poltrona ocupada
        ROLLBACK;
        SET p_IngressoId = -1; -- Código de erro para "Poltrona Ocupada"
    END IF;
END $$

-- ---------------------------------------------------------
-- PROCEDURE: Cancelar Reserva
-- ---------------------------------------------------------
CREATE PROCEDURE sp_CancelarReserva(
    IN p_IngressoId INT,
    OUT p_Resultado VARCHAR(100)
)
BEGIN
    DECLARE v_PoltronaId INT;
    DECLARE v_StatusAtual VARCHAR(100);

    START TRANSACTION;
    SELECT StatusIngresso, PoltronaId INTO v_StatusAtual, v_PoltronaId
    FROM Ingresso WHERE IngressoId = p_IngressoId FOR UPDATE;

    IF v_StatusAtual IS NULL THEN
        ROLLBACK;
        SET p_Resultado = 'Reserva não encontrada.';
    ELSEIF v_StatusAtual = 'CANCELADA' THEN
        ROLLBACK;
        SET p_Resultado = 'Reserva já estava cancelada.';
    ELSE
        UPDATE Ingresso SET StatusIngresso = 'CANCELADA' WHERE IngressoId = p_IngressoId;
        UPDATE Poltrona SET Disponibilidade = 1 WHERE PoltronaId = v_PoltronaId;
        COMMIT;
        SET p_Resultado = 'Reserva cancelada com sucesso!';
    END IF;
END $$

-- ---------------------------------------------------------
-- PROCEDURES DE SERVIÇO (LIMPEZA E MANUTENÇÃO)
-- ---------------------------------------------------------

-- 1. Iniciar Limpeza: Bloqueia a Sala
CREATE PROCEDURE sp_IniciarLimpeza(IN p_SalaId INT)
BEGIN
    -- Bloqueia a sala para vendas
    UPDATE Sala SET Disponivel = 0 WHERE SalaId = p_SalaId;

    -- Registra o início
    INSERT INTO Limpeza (DataLimpeza, StatusLimpeza, SalaId) 
    VALUES (NOW(), 'EM ANDAMENTO', p_SalaId);
END $$

-- 2. Finalizar Limpeza: Libera a Sala
CREATE PROCEDURE sp_FinalizarLimpeza(IN p_SalaId INT)
BEGIN
    -- Libera a sala para vendas
    UPDATE Sala SET Disponivel = 1 WHERE SalaId = p_SalaId;

    -- Atualiza o registro que estava em andamento
    UPDATE Limpeza SET StatusLimpeza = 'Concluído' 
    WHERE SalaId = p_SalaId AND StatusLimpeza = 'EM ANDAMENTO';
END $$

-- 3. Iniciar Manutenção: Bloqueia a Sala
CREATE PROCEDURE sp_IniciarManutencao(IN p_SalaId INT)
BEGIN
    UPDATE Sala SET Disponivel = 0 WHERE SalaId = p_SalaId;

    INSERT INTO Manutencao (DataManutencao, StatusManutencao, SalaId) 
    VALUES (NOW(), 'EM ANDAMENTO', p_SalaId);
END $$

-- 4. Finalizar Manutenção: Libera a Sala
CREATE PROCEDURE sp_FinalizarManutencao(IN p_SalaId INT)
BEGIN
    UPDATE Sala SET Disponivel = 1 WHERE SalaId = p_SalaId;

    UPDATE Manutencao SET StatusManutencao = 'Concluído' 
    WHERE SalaId = p_SalaId AND StatusManutencao = 'EM ANDAMENTO';
END $$

DELIMITER ;