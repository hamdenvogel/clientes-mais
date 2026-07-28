-- DDL para reestruturacao do campo foto de Cliente e Prestador
-- Banco alvo: PostgreSQL 16.3
-- Schema: meusservicos
--
-- Objetivo:
-- 1. Criar nova tabela de metadata de foto de perfil
-- 2. Garantir 1 foto ativa por dono (cliente ou prestador)
-- 3. Manter historico de fotos
-- 4. Preparar migracao do modelo legado baseado em meusservicos.imagem

BEGIN;

CREATE SCHEMA IF NOT EXISTS meusservicos;

-- -----------------------------------------------------------------------------
-- 1) Tipo do dono da foto
--
-- Usamos varchar + check para evitar dependencia de type customizado e tornar
-- a migracao mais simples em ambientes que ja tenham transacoes pendentes.
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 2) Tabela principal de fotos de perfil
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS meusservicos.foto_perfil (
    id                  bigserial PRIMARY KEY,
    owner_type          varchar(20) NOT NULL,
    owner_id            bigint NOT NULL,

    storage_provider    varchar(30) NOT NULL,
    object_key          varchar(500) NOT NULL,

    file_name_original  varchar(255),
    mime_type           varchar(100) NOT NULL,
    data                bytea,
    size_bytes          bigint NOT NULL,
    sha256              varchar(64),
    largura             integer,
    altura              integer,

    ativa               boolean NOT NULL DEFAULT true,

    created_at          timestamp without time zone NOT NULL DEFAULT now(),
    updated_at          timestamp without time zone NOT NULL DEFAULT now(),
    created_by          varchar(120),
    updated_by          varchar(120),

    CONSTRAINT ck_foto_perfil_size_positive
        CHECK (size_bytes > 0),

    CONSTRAINT ck_foto_perfil_owner_type
        CHECK (owner_type IN ('CLIENTE', 'PRESTADOR')),

    CONSTRAINT ck_foto_perfil_storage_provider
        CHECK (storage_provider IN ('S3', 'AZURE_BLOB', 'MINIO', 'DB')),

    CONSTRAINT ck_foto_perfil_mime
        CHECK (mime_type IN ('image/jpeg', 'image/png', 'image/webp'))
);

-- -----------------------------------------------------------------------------
-- 3) Indices
-- -----------------------------------------------------------------------------
CREATE UNIQUE INDEX IF NOT EXISTS ux_foto_perfil_object_key
    ON meusservicos.foto_perfil (object_key);

CREATE UNIQUE INDEX IF NOT EXISTS ux_foto_perfil_owner_ativa
    ON meusservicos.foto_perfil (owner_type, owner_id)
    WHERE ativa = true;

CREATE INDEX IF NOT EXISTS ix_foto_perfil_owner
    ON meusservicos.foto_perfil (owner_type, owner_id);

CREATE INDEX IF NOT EXISTS ix_foto_perfil_owner_ativa
    ON meusservicos.foto_perfil (owner_type, owner_id, ativa);

CREATE INDEX IF NOT EXISTS ix_foto_perfil_created_at
    ON meusservicos.foto_perfil (created_at);

-- Garante compatibilidade em ambientes onde a tabela ja existia sem a coluna binaria.
ALTER TABLE meusservicos.foto_perfil
    ADD COLUMN IF NOT EXISTS data bytea;

-- -----------------------------------------------------------------------------
-- 4) Trigger para updated_at
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION meusservicos.fn_set_updated_at_foto_perfil()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.updated_at := now();
    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_set_updated_at_foto_perfil ON meusservicos.foto_perfil;

CREATE TRIGGER trg_set_updated_at_foto_perfil
BEFORE UPDATE ON meusservicos.foto_perfil
FOR EACH ROW
EXECUTE FUNCTION meusservicos.fn_set_updated_at_foto_perfil();

-- -----------------------------------------------------------------------------
-- 5) Migracao do legado (imagem -> foto_perfil)
--
-- Premissas:
-- - meusservicos.documento.descricao = 'C' para Cliente
-- - meusservicos.documento.descricao = 'P' para Prestador
-- - meusservicos.imagem.chave_id contem o id do dono
-- - meusservicos.imagem.documento_id aponta para documento
--
-- Se houver multiplas imagens por dono, o criterio abaixo preserva todas como
-- historico, marcando a mais recente como ativa.
-- -----------------------------------------------------------------------------
WITH ranked_legacy AS (
    SELECT
        i.id,
        CASE d.descricao
            WHEN 'C' THEN 'CLIENTE'
            WHEN 'P' THEN 'PRESTADOR'
        END AS owner_type,
        i.chave_id::bigint AS owner_id,
        'DB'::varchar(30) AS storage_provider,
        CONCAT('legacy/', i.id) AS object_key,
        i.original_file_name,
        COALESCE(i.file_type, 'image/jpeg') AS mime_type,
        COALESCE(i.size, 1) AS size_bytes,
        NULL::varchar(64) AS sha256,
        NULL::integer AS largura,
        NULL::integer AS altura,
        i.created_date,
        i.updated_date,
        i.created_by,
        i.updated_by,
        ROW_NUMBER() OVER (
            PARTITION BY CASE d.descricao
                WHEN 'C' THEN 'CLIENTE'
                WHEN 'P' THEN 'PRESTADOR'
            END,
            i.chave_id
            ORDER BY COALESCE(i.updated_date, i.created_date, now()) DESC, i.id DESC
        ) AS rn
    FROM meusservicos.imagem i
    JOIN meusservicos.documento d ON d.id = i.documento_id
    WHERE i.chave_id IS NOT NULL
      AND d.descricao IN ('C', 'P')
)
INSERT INTO meusservicos.foto_perfil (
    owner_type,
    owner_id,
    storage_provider,
    object_key,
    file_name_original,
    mime_type,
    size_bytes,
    sha256,
    largura,
    altura,
    ativa,
    created_at,
    updated_at,
    created_by,
    updated_by
)
SELECT
    owner_type,
    owner_id,
    storage_provider,
    object_key,
    original_file_name,
    mime_type,
    size_bytes,
    sha256,
    largura,
    altura,
    CASE WHEN rn = 1 THEN true ELSE false END AS ativa,
    COALESCE(created_date, now()) AS created_at,
    COALESCE(updated_date, now()) AS updated_at,
    created_by,
    updated_by
FROM ranked_legacy
WHERE owner_type IS NOT NULL;

COMMIT;
