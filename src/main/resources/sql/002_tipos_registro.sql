CREATE TABLE IF NOT EXISTS tipos_registro (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tipos_registro (nome)
VALUES
    ('Assunto'),
    ('Questões'),
    ('Resumo'),
    ('Simulado'),
    ('Prova')
ON CONFLICT (nome) DO NOTHING;

ALTER TABLE registros
ADD COLUMN IF NOT EXISTS tipo_registro_fk BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_registros_tipo_registro'
    ) THEN
        ALTER TABLE registros
        ADD CONSTRAINT fk_registros_tipo_registro
        FOREIGN KEY (tipo_registro_fk)
        REFERENCES tipos_registro(id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_registros_tipo_registro_data
ON registros (tipo_registro_fk, data_estudo DESC);
