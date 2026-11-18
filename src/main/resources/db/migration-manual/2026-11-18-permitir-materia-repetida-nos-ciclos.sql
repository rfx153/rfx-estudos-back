BEGIN;

ALTER TABLE public.ciclo_itens
    DROP CONSTRAINT IF EXISTS uk_ciclo_itens_materia;

COMMIT;
