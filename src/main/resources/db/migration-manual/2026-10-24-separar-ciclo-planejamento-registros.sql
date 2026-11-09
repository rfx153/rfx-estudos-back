BEGIN;

-- A coluna então chamada ciclo_fk apontava para o material-guia já cadastrado.
-- Ela é renomeada para material_guia_fk pela migração de 2026-11-09.
-- planejamento_fk passa a apontar para o planejamento de estudos.
ALTER TABLE public.registros
    ADD COLUMN IF NOT EXISTS planejamento_fk BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_registros_planejamento'
          AND conrelid = 'public.registros'::regclass
    ) THEN
        ALTER TABLE public.registros
            ADD CONSTRAINT fk_registros_planejamento
            FOREIGN KEY (planejamento_fk)
            REFERENCES public.planejamentos (id)
            ON DELETE SET NULL;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_registros_planejamento_data
    ON public.registros (planejamento_fk, data_estudo DESC);

COMMIT;
