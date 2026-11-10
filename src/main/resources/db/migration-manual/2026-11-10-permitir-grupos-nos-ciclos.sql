BEGIN;

ALTER TABLE public.ciclo_materias RENAME TO ciclo_itens;

ALTER TABLE public.ciclo_itens
    ADD COLUMN planejamento_grupo_materia_fk BIGINT,
    ALTER COLUMN materia_fk DROP NOT NULL;

ALTER TABLE public.ciclo_itens
    DROP CONSTRAINT IF EXISTS uk_ciclo_materias_materia,
    DROP CONSTRAINT IF EXISTS uk_ciclo_materias_ordem;

ALTER TABLE public.ciclo_itens
    ADD CONSTRAINT fk_ciclo_itens_grupo
        FOREIGN KEY (planejamento_grupo_materia_fk)
        REFERENCES public.planejamento_grupos_materias (id),
    ADD CONSTRAINT ck_ciclo_itens_tipo
        CHECK (num_nonnulls(materia_fk, planejamento_grupo_materia_fk) = 1),
    ADD CONSTRAINT uk_ciclo_itens_materia
        UNIQUE (ciclo_fk, materia_fk),
    ADD CONSTRAINT uk_ciclo_itens_grupo
        UNIQUE (ciclo_fk, planejamento_grupo_materia_fk),
    ADD CONSTRAINT uk_ciclo_itens_ordem
        UNIQUE (ciclo_fk, ordem);

ALTER INDEX IF EXISTS public.idx_ciclo_materias_ciclo_ordem
    RENAME TO idx_ciclo_itens_ciclo_ordem;

COMMIT;
