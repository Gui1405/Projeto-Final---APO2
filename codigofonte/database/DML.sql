-- Script de Carga Inicial DML
USE cinema;

INSERT INTO cliente (nome, email, senha, cpf, validado, perfil) VALUES
('Administrador', 'admin@cinema.com', 'admin123', '00000000000', TRUE, 'ADMIN'),
('Usuario Teste', 'teste@cinema.com', 'teste123', '11111111111', TRUE, 'USER');

INSERT INTO filme (titulo, duracao, genero, classificacao, sinopse) VALUES
('Vingadores: Ultimato', 181, 'Ação/Ficção', '12 anos', 'Os heróis sobreviventes tentam reverter as ações de Thanos.'),
('O Auto da Compadecida 2', 120, 'Comédia', '10 anos', 'Novas aventuras de João Grilo e Chicó.'),
('Coringa: Delírio a Dois', 138, 'Drama/Suspense', '16 anos', 'Arthur Fleck conhece Harley Quinn em Arkham.');

INSERT INTO sala (nome, capacidade) VALUES
('Sala 1 - IMAX', 200),
('Sala 2 - VIP', 50),
('Sala 3 - Standard', 150);

INSERT INTO sessao (filme_id, sala_id, horario, valor_ingresso) VALUES
(1, 1, '2026-06-01 19:00:00', 45.00),
(2, 3, '2026-06-01 20:30:00', 25.00),
(3, 2, '2026-06-01 22:00:00', 60.00);

-- Inserção de poltronas
INSERT INTO poltrona (numero, disponivel, sala_id) VALUES 
('A1', 1, 1), ('A2', 1, 1), ('A3', 1, 1), ('A4', 1, 1), ('A5', 1, 1), 
('B1', 1, 1), ('B2', 1, 1), ('B3', 1, 1), ('B4', 1, 1), ('B5', 1, 1), 
('C1', 1, 1), ('C2', 1, 1), ('C3', 1, 1), ('C4', 1, 1), ('C5', 1, 1), 
('D1', 1, 1), ('D2', 1, 1), ('D3', 1, 1), ('D4', 1, 1), ('D5', 1, 1);

INSERT INTO poltrona (numero, disponivel, sala_id) VALUES 
('A1', 1, 2), ('A2', 1, 2), ('A3', 1, 2), ('A4', 1, 2), ('A5', 1, 2);
