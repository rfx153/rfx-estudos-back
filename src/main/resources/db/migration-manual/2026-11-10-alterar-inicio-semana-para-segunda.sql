BEGIN;

ALTER TABLE public.metas_semanais
    DROP CONSTRAINT IF EXISTS ck_metas_semanais_inicio_domingo;

ALTER TABLE public.metas_semanais
    DROP CONSTRAINT IF EXISTS ck_metas_semanais_inicio_segunda;

UPDATE public.metas_semanais
SET inicio_semana = inicio_semana + 1
WHERE EXTRACT(DOW FROM inicio_semana) = 0;

ALTER TABLE public.metas_semanais
    ADD CONSTRAINT ck_metas_semanais_inicio_segunda
        CHECK (EXTRACT(DOW FROM inicio_semana) = 1);

COMMIT;
