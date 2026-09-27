CREATE DATABASE alistamento_militar;
use alistamento_militar;

CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    cpf CHAR(11) NOT NULL UNIQUE,          
    nome_pai VARCHAR(100),
    nome_mae VARCHAR(100) NOT NULL,
    estado_civil VARCHAR(20),
    uf CHAR(2),
    escolaridade VARCHAR(50),
    rg VARCHAR(20) NOT NULL,
    local_nascimento VARCHAR(100),
    cep VARCHAR(10),
    bairro VARCHAR(100),
    municipio VARCHAR(100),
    pais_residencia VARCHAR(50),
    zona_residencial VARCHAR(50),
    numero_residencia VARCHAR(10),
    logradouro VARCHAR(150),
    estado VARCHAR(50),
    
    -- ALTERADO: verifica se o CPF possui exatamente 11 dígitos numéricos
    CONSTRAINT ck_usuario_cpf CHECK (cpf REGEXP '^[0-9]{11}$')
);



CREATE TABLE Administrador (
    id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nome_admin VARCHAR(100) NOT NULL,
    email_admin VARCHAR(100) NOT NULL UNIQUE,
    senha_admin VARCHAR(255) NOT NULL
);

CREATE TABLE Alistamento (
    id_alistamento INT AUTO_INCREMENT PRIMARY KEY,
    data_alistamento DATE,
    status VARCHAR(50) DEFAULT 'Aguardando documentos',
    id_usuario INT NOT NULL UNIQUE,
    id_admin INT NULL,
    
   CONSTRAINT fk_alistamento_usuario
   FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
    ON DELETE RESTRICT   -- não deixa deletar usuário com alistamento
    ON UPDATE CASCADE,   -- se o id atualizar, propaga nas outras tabelas
    
   CONSTRAINT fk_alistamento_admin
    FOREIGN KEY (id_admin) REFERENCES Administrador(id_admin)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE TABLE TipoDocumento (
    id_tipo_documento INT AUTO_INCREMENT PRIMARY KEY,
    nome_tipo VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT
);

CREATE TABLE Documento (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    numero_documento VARCHAR(50) NOT NULL,
    numero_folha VARCHAR(50),
    numero_livro VARCHAR(50),
    data_emissao DATE NOT NULL,
    orgao_emissor VARCHAR(100) NOT NULL,
    cidade_emissao VARCHAR(100) NOT NULL,
    estado_emissao VARCHAR(50) NOT NULL,
    nome_arquivo VARCHAR(100) NOT NULL,
    data_envio DATETIME NOT NULL,
    -- Usada pelo admin pra aprovar/reprovar ('Aprovado'/'Reprovado'/'Em análise')
    status VARCHAR(20) DEFAULT 'Em análise' NOT NULL,
    id_usuario INT NOT NULL,
    id_alistamento INT NOT NULL,
    id_tipo_documento INT NOT NULL,
    
    CONSTRAINT fk_documento_usuario
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
    ON DELETE RESTRICT
	ON UPDATE CASCADE,
    
    CONSTRAINT fk_documento_alistamento
	FOREIGN KEY (id_alistamento) REFERENCES Alistamento(id_alistamento)
    ON DELETE RESTRICT   -- não deixa deletar alistamento com documentos
    ON UPDATE CASCADE,
    
    CONSTRAINT fk_documento_tipo
	FOREIGN KEY (id_tipo_documento) REFERENCES TipoDocumento(id_tipo_documento)
);

CREATE TABLE Local (
    id_local INT AUTO_INCREMENT PRIMARY KEY,
    nome_unidade VARCHAR(100) NOT NULL,
    endereco_local VARCHAR(150) NOT NULL,
    cidade_local VARCHAR(100) NOT NULL,
    estado_local VARCHAR(50) NOT NULL,
    cep_local VARCHAR(10) NOT NULL
);

CREATE TABLE Medico (
    id_medico INT AUTO_INCREMENT PRIMARY KEY,
    nome_medico VARCHAR(100) NOT NULL,
    crm VARCHAR(20) NOT NULL UNIQUE,
    especialidade VARCHAR(100),
    telefone_medico VARCHAR(20),
    email_medico VARCHAR(100) NOT NULL UNIQUE,
    senha_medico VARCHAR(255) NOT NULL
);

CREATE TABLE AvaliacaoMedica (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    data_avaliacao DATE NOT NULL,
    resultado VARCHAR(100) NOT NULL,
    observacoes TEXT,
    id_alistamento INT NOT NULL UNIQUE,
    id_medico INT NOT NULL,
    id_local INT NOT NULL,
    
    CONSTRAINT fk_avaliacao_alistamento
    FOREIGN KEY (id_alistamento) REFERENCES Alistamento(id_alistamento)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
    
    CONSTRAINT fk_avaliacao_medico
	FOREIGN KEY (id_medico) REFERENCES Medico(id_medico),
    CONSTRAINT fk_avaliacao_local
	FOREIGN KEY (id_local) REFERENCES Local(id_local),
    
    -- Permite apenas os resultados: Apto, Inapto ou Em análise
    CHECK (resultado IN ('Apto', 'Inapto', 'Em análise'))
);

CREATE TABLE Agendamento (
    id_agendamento INT AUTO_INCREMENT PRIMARY KEY,
    data_agendamento DATE NOT NULL,
    horario TIME NOT NULL,
    id_alistamento INT NOT NULL UNIQUE, -- (1,1) cada alistamento tem 1 agendamento
    id_local INT NOT NULL,
    id_medico INT NOT NULL,
    -- Vira true quando o usuário confirma presença
     confirmado BOOLEAN NOT NULL DEFAULT FALSE,
     status VARCHAR(20) DEFAULT 'Agendado' NOT NULL,
     
	CONSTRAINT fk_agendamento_alistamento
	FOREIGN KEY (id_alistamento) REFERENCES Alistamento(id_alistamento)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
    
    CONSTRAINT fk_agendamento_local
	FOREIGN KEY (id_local) REFERENCES Local(id_local),
    
    CONSTRAINT fk_agendamento_medico
    FOREIGN KEY (id_medico) REFERENCES Medico(id_medico)
    
    
);







