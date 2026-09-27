ALTER TABLE planejamentos
ADD COLUMN IF NOT EXISTS descricao TEXT,
ADD COLUMN IF NOT EXISTS data_inicio DATE,
ADD COLUMN IF NOT EXISTS data_prevista DATE,
ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'Ativo',
ADD COLUMN IF NOT EXISTS data_finalizacao DATE;

UPDATE planejamentos
SET status = 'Ativo'
WHERE status IS NULL OR TRIM(status) = '';

CREATE TABLE IF NOT EXISTS planejamento_itens (
    id BIGSERIAL PRIMARY KEY,
    planejamento_fk BIGINT NOT NULL,
    materia_fk BIGINT NOT NULL,
    assunto_fk BIGINT NULL,
    material_tipo_fk BIGINT NULL,
    material_nome VARCHAR(255) NULL,
    prioridade VARCHAR(50) DEFAULT 'Media',
    meta TEXT NULL,
    data_prevista DATE NULL,
    link_documento TEXT NULL,
    status VARCHAR(50) DEFAULT 'Pendente',
    data_finalizacao DATE NULL,
    ordem INTEGER NULL,
    observacoes TEXT NULL,
    CONSTRAINT fk_planejamento_itens_planejamento
        FOREIGN KEY (planejamento_fk) REFERENCES planejamentos(id) ON DELETE CASCADE,
    CONSTRAINT fk_planejamento_itens_materia
        FOREIGN KEY (materia_fk) REFERENCES materias(id),
    CONSTRAINT fk_planejamento_itens_assunto
        FOREIGN KEY (assunto_fk) REFERENCES assuntos(id),
    CONSTRAINT fk_planejamento_itens_material_tipo
        FOREIGN KEY (material_tipo_fk) REFERENCES material_tipos(id)
);

CREATE INDEX IF NOT EXISTS idx_planejamento_itens_planejamento
ON planejamento_itens (planejamento_fk, ordem, id);

CREATE INDEX IF NOT EXISTS idx_planejamento_itens_materia
ON planejamento_itens (materia_fk);

CREATE INDEX IF NOT EXISTS idx_planejamento_itens_assunto
ON planejamento_itens (assunto_fk);

CREATE TABLE IF NOT EXISTS planejamento_ciclos (
    id BIGSERIAL PRIMARY KEY,
    planejamento_fk BIGINT NOT NULL,
    ciclo_fk BIGINT NOT NULL,
    CONSTRAINT uk_planejamento_ciclos_planejamento_ciclo UNIQUE (planejamento_fk, ciclo_fk),
    CONSTRAINT fk_planejamento_ciclos_planejamento
        FOREIGN KEY (planejamento_fk) REFERENCES planejamentos(id) ON DELETE CASCADE,
    CONSTRAINT fk_planejamento_ciclos_ciclo
        FOREIGN KEY (ciclo_fk) REFERENCES ciclos_estudo(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_planejamento_ciclos_planejamento
ON planejamento_ciclos (planejamento_fk);

CREATE INDEX IF NOT EXISTS idx_planejamento_ciclos_ciclo
ON planejamento_ciclos (ciclo_fk);

-- Compatibilidade com a primeira modelagem de planejamentos.
-- O planejamento macro deixou de ter uma única matéria/assunto/material obrigatórios;
-- esses dados agora vivem em planejamento_itens.
ALTER TABLE planejamentos
ALTER COLUMN materia_fk DROP NOT NULL,
ALTER COLUMN assunto_fk DROP NOT NULL,
ALTER COLUMN material_tipo_fk DROP NOT NULL;
