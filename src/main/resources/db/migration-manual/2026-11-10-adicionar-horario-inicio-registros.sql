BEGIN;

ALTER TABLE public.registros
    ADD COLUMN horario_inicio TIME;

COMMIT;
