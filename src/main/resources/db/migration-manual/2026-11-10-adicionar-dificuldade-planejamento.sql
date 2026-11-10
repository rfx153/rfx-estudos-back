BEGIN;

ALTER TABLE public.planejamento_materias
    ADD COLUMN dificuldade SMALLINT;

ALTER TABLE public.planejamento_materias
    ADD CONSTRAINT ck_plan_materias_dificuldade
        CHECK (dificuldade IS NULL OR dificuldade BETWEEN 1 AND 5);

ALTER TABLE public.planejamento_grupo_assunto_itens
    ADD COLUMN dificuldade SMALLINT;

ALTER TABLE public.planejamento_grupo_assunto_itens
    ADD CONSTRAINT ck_plan_grupo_itens_dificuldade
        CHECK (dificuldade IS NULL OR dificuldade BETWEEN 1 AND 5);

COMMIT;
