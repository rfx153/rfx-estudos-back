ALTER TABLE planejamentos
ADD COLUMN IF NOT EXISTS nome VARCHAR(150);

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'planejamentos'
          AND column_name = 'nome_planejamento'
    ) THEN
        UPDATE planejamentos
        SET nome = nome_planejamento
        WHERE nome IS NULL;
    END IF;
END $$;

UPDATE planejamentos
SET nome = CONCAT('Planejamento ', id)
WHERE nome IS NULL OR TRIM(nome) = '';

WITH planejamentos_duplicados AS (
    SELECT id, nome, ROW_NUMBER() OVER (PARTITION BY nome ORDER BY id) AS ordem
    FROM planejamentos
)
UPDATE planejamentos p
SET nome = CONCAT(p.nome, ' #', p.id)
FROM planejamentos_duplicados d
WHERE p.id = d.id
  AND d.ordem > 1;

ALTER TABLE planejamentos
ALTER COLUMN nome SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_planejamentos_nome'
    ) THEN
        ALTER TABLE planejamentos
        ADD CONSTRAINT uk_planejamentos_nome
        UNIQUE (nome);
    END IF;
END $$;
