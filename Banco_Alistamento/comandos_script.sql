-- testes.sql
-- Consultas para o dia a dia. NÃO rode o arquivo inteiro:
-- coloque o cursor na linha e use o raio com cursor (⚡I) para rodar só ela.
 
USE alistamento_militar;
 
-- Ver estrutura
SHOW TABLES;
SHOW PROCEDURE STATUS WHERE Db = 'alistamento_militar';
 
-- Ver dados
SELECT * FROM Usuario;
SELECT * FROM Alistamento;
SELECT * FROM Documento;
SELECT * FROM Agendamento;
SELECT * FROM AvaliacaoMedica;
SELECT * FROM Administrador;
SELECT * FROM Medico;
SELECT * FROM Local;
SELECT * FROM TipoDocumento;
 
-- Buscar um usuário específico (troque o 4 pelo id que quiser)
SELECT * FROM Usuario WHERE id_usuario = 4;
SELECT * FROM Alistamento WHERE id_usuario = 4;
 
-- Contar registros
SELECT COUNT(*) AS total_usuarios FROM Usuario;
 
-- =====================================================================
-- CUIDADO: apaga TODOS os dados (as tabelas continuam existindo).
-- Tire o "-- " da frente só quando quiser mesmo limpar.
-- Depois, rode o 03_dados_iniciais.sql de novo.
-- =====================================================================
-- CALL resetar_banco();