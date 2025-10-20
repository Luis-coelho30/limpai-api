-- ===============================
-- Schema do projeto Limpai
-- ===============================

-- Tabela estado
CREATE TABLE "estado" (
    "estado_id" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "nome" VARCHAR(100) NOT NULL,
    "sigla" CHAR(2) UNIQUE NOT NULL
);

-- Tabela cidade
CREATE TABLE "cidade" (
    "cidade_id" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "nome" VARCHAR(100) NOT NULL,
    "estado_id" BIGINT NOT NULL,
    FOREIGN KEY ("estado_id") REFERENCES "estado"("estado_id")
);

-- Tabela local
CREATE TABLE "local" (
    "local_id" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "nome" VARCHAR(150) NOT NULL,
    "endereco" VARCHAR(200) NOT NULL,
    "cidade_id" BIGINT NOT NULL,
    "cep" CHAR(8) NOT NULL,
    CONSTRAINT fk_local_cidade FOREIGN KEY ("cidade_id") REFERENCES "cidade"("cidade_id"),
    CONSTRAINT uq_local_endereco_cep UNIQUE ("endereco", "cep")
);

-- Índice para otimizar consultas por cidade
CREATE INDEX idx_local_cidade_id ON "local" ("cidade_id");


-- Tabela usuario
CREATE TABLE "usuario" (
    "usuario_id" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "email" VARCHAR(150) NOT NULL UNIQUE,
    "senha" VARCHAR(64),
    "telefone" VARCHAR(20),
    "tipo" VARCHAR(12) NOT NULL
);

-- Tabela voluntario
CREATE TABLE "voluntario" (
    "usuario_id" BIGINT PRIMARY KEY,
    "nome" VARCHAR(100) NOT NULL,
    "cpf" VARCHAR(14) NOT NULL,
    "data_nascimento" TIMESTAMP,
    FOREIGN KEY ("usuario_id") REFERENCES "usuario"("usuario_id")
);

-- Tabela patrocinador
CREATE TABLE "patrocinador" (
    "usuario_id" BIGINT PRIMARY KEY,
    "razao_social" VARCHAR(150) NOT NULL,
    "nome_fantasia" VARCHAR(150),
    "cnpj" CHAR(14) UNIQUE,
    FOREIGN KEY ("usuario_id") REFERENCES "usuario"("usuario_id")
);

-- Tabela campanha
CREATE TABLE "campanha" (
    "campanha_id" BIGINT AUTO_INCREMENT PRIMARY KEY,
    "nome" VARCHAR(100) NOT NULL,
    "descricao" TEXT,
    "data_inicio" TIMESTAMP,
    "data_fim" TIMESTAMP,
    "meta_fundos" DECIMAL(10,2) DEFAULT 0.00,
    "fundos_arrecadados" DECIMAL(10,2) DEFAULT 0.00,
    "local_id" BIGINT NOT NULL,
    FOREIGN KEY ("local_id") REFERENCES "local"("local_id")
);

-- Tabela usuario_campanha
CREATE TABLE "usuario_campanha" (
    "campanha_id" BIGINT NOT NULL,
    "usuario_id" BIGINT NOT NULL,
    "data_inscricao" TIMESTAMP,
    PRIMARY KEY ("campanha_id", "usuario_id"),
    FOREIGN KEY ("campanha_id") REFERENCES "campanha"("campanha_id"),
    FOREIGN KEY ("usuario_id") REFERENCES "usuario"("usuario_id")
);
