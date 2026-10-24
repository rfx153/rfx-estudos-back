BEGIN;

-- ciclo_fk continua apontando para o material-guia já cadastrado.
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
