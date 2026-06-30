-- Script de Carga Inicial DML
USE cinema;

INSERT INTO cliente (id, nome, email, senha, cpf, validado, perfil) VALUES
(1, 'Administrador', 'admin@cinema.com', 'admin123', '00000000000', TRUE, 'ADMIN'),
(2, 'Usuario Teste', 'teste@cinema.com', 'teste123', '11111111111', TRUE, 'USER');

INSERT INTO filme (id, titulo, duracao, genero, classificacao, sinopse) VALUES
(1, 'Vingadores: Ultimato', 181, 'Ação/Ficção', '12 anos', 'Os heróis sobreviventes tentam reverter as ações de Thanos.'),
(2, 'O Auto da Compadecida 2', 120, 'Comédia', '10 anos', 'Novas aventuras de João Grilo e Chicó.'),
(3, 'Coringa: Delírio a Dois', 138, 'Drama/Suspense', '16 anos', 'Arthur Fleck conhece Harley Quinn em Arkham.'),
(4, 'Matrix (Remastered)', 136, 'Ficção Científica', '14 anos', 'Um programador descobre que o mundo em que vive é uma simulação.');

-- Sala 2 configurada como indisponível (em andamento de limpeza e manutenção)
INSERT INTO sala (id, nome, capacidade, disponivel) VALUES
(1, 'Sala 1 - IMAX', 20, 1),
(2, 'Sala 2 - VIP', 20, 0),
(3, 'Sala 3 - Standard', 20, 1);

INSERT INTO sessao (id, filme_id, sala_id, horario, valor_ingresso) VALUES
(1, 1, 1, DATE_ADD(NOW(), INTERVAL 2 HOUR), 45.00),
(2, 2, 3, DATE_ADD(NOW(), INTERVAL 4 HOUR), 25.00),
(3, 3, 2, DATE_ADD(NOW(), INTERVAL 5 HOUR), 60.00),
(4, 4, 1, DATE_ADD(NOW(), INTERVAL 7 HOUR), 40.00);

-- Poltronas das Salas 1, 2 e 3 (Poltronas A1 e A2 da Sala 1 ocupadas para teste)
INSERT INTO poltrona (id, numero, disponivel, sala_id) VALUES 
(1, 'A1', 0, 1), (2, 'A2', 0, 1), (3, 'A3', 1, 1), (4, 'A4', 1, 1), (5, 'A5', 1, 1), 
(6, 'B1', 1, 1), (7, 'B2', 1, 1), (8, 'B3', 1, 1), (9, 'B4', 1, 1), (10, 'B5', 1, 1), 
(11, 'C1', 1, 1), (12, 'C2', 1, 1), (13, 'C3', 1, 1), (14, 'C4', 1, 1), (15, 'C5', 1, 1), 
(16, 'D1', 1, 1), (17, 'D2', 1, 1), (18, 'D3', 1, 1), (19, 'D4', 1, 1), (20, 'D5', 1, 1),
(21, 'A1', 1, 2), (22, 'A2', 1, 2), (23, 'A3', 1, 2), (24, 'A4', 1, 2), (25, 'A5', 1, 2), 
(26, 'B1', 1, 2), (27, 'B2', 1, 2), (28, 'B3', 1, 2), (29, 'B4', 1, 2), (30, 'B5', 1, 2), 
(31, 'C1', 1, 2), (32, 'C2', 1, 2), (33, 'C3', 1, 2), (34, 'C4', 1, 2), (35, 'C5', 1, 2), 
(36, 'D1', 1, 2), (37, 'D2', 1, 2), (38, 'D3', 1, 2), (39, 'D4', 1, 2), (40, 'D5', 1, 2),
(41, 'A1', 1, 3), (42, 'A2', 1, 3), (43, 'A3', 1, 3), (44, 'A4', 1, 3), (45, 'A5', 1, 3), 
(46, 'B1', 1, 3), (47, 'B2', 1, 3), (48, 'B3', 1, 3), (49, 'B4', 1, 3), (50, 'B5', 1, 3), 
(51, 'C1', 1, 3), (52, 'C2', 1, 3), (53, 'C3', 1, 3), (54, 'C4', 1, 3), (55, 'C5', 1, 3), 
(56, 'D1', 1, 3), (57, 'D2', 1, 3), (58, 'D3', 1, 3), (59, 'D4', 1, 3), (60, 'D5', 1, 3);

-- Ativando Limpeza e Manutenção para a Sala 2 (VIP)
INSERT INTO limpeza (data_limpeza, status, observacao, sala_id) VALUES 
(NOW(), 'EM ANDAMENTO', 'Limpeza profunda e higienização geral pós-sessão', 2);

INSERT INTO manutencao (data_manutencao, status, observacao, sala_id) VALUES 
(NOW(), 'EM ANDAMENTO', 'Troca da lâmpada do projetor principal e calibração de áudio', 2);

-- Ingressos comprados para o Usuario Teste (cliente_id = 2) na Sessao 1
INSERT INTO ingresso (status, data_compra, cliente_id, sessao_id, poltrona_id) VALUES 
('COMPRADO', NOW(), 2, 1, 1),
('COMPRADO', NOW(), 2, 1, 2);
