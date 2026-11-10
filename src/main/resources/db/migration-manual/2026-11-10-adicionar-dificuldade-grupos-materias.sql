BEGIN;

ALTER TABLE public.planejamento_grupos_materias
    ADD COLUMN dificuldade SMALLINT;

ALTER TABLE public.planejamento_grupos_materias
    ADD CONSTRAINT ck_plan_grupos_materias_dificuldade
        CHECK (dificuldade IS NULL OR dificuldade BETWEEN 1 AND 5);

COMMIT;
