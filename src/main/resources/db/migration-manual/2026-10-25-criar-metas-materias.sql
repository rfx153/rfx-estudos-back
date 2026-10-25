CREATE TABLE IF NOT EXISTS metas_materias (
    id BIGSERIAL PRIMARY KEY,
    materia_fk BIGINT NOT NULL UNIQUE,
    meta_semanal_minutos INTEGER NOT NULL,
    CONSTRAINT fk_metas_materias_materia
        FOREIGN KEY (materia_fk) REFERENCES materias(id)
        ON DELETE CASCADE,
    CONSTRAINT ck_metas_materias_meta_semanal
        CHECK (meta_semanal_minutos > 0)
);

CREATE INDEX IF NOT EXISTS idx_registros_materia_data_estudo
    ON registros (materia_fk, data_estudo);
