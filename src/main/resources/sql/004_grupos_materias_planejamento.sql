CREATE TABLE IF NOT EXISTS planejamento_grupos_materias (
    id BIGSERIAL PRIMARY KEY,
    planejamento_fk BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    peso SMALLINT NOT NULL DEFAULT 3,
    ordem INTEGER,
    data_prevista DATE,
    data_finalizada DATE,
    observacoes TEXT,
    CONSTRAINT fk_planejamento_grupos_materias_planejamento
        FOREIGN KEY (planejamento_fk) REFERENCES planejamentos(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_planejamento_grupos_materias_planejamento_ordem
    ON planejamento_grupos_materias (planejamento_fk, ordem);

ALTER TABLE planejamento_materias
    ADD COLUMN IF NOT EXISTS planejamento_grupo_materia_fk BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'fk_planejamento_materias_grupo_materia'
    ) THEN
        ALTER TABLE planejamento_materias
            ADD CONSTRAINT fk_planejamento_materias_grupo_materia
            FOREIGN KEY (planejamento_grupo_materia_fk)
            REFERENCES planejamento_grupos_materias(id)
            ON DELETE SET NULL;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_planejamento_materias_grupo_materia
    ON planejamento_materias (planejamento_grupo_materia_fk);
