BEGIN;

CREATE TABLE IF NOT EXISTS public.planejamentos (
    id BIGSERIAL,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,

    CONSTRAINT pk_planejamentos
        PRIMARY KEY (id),
    CONSTRAINT uk_planejamentos_nome
        UNIQUE (nome)
);

-- Mantem o script compativel caso planejamentos tenha sido criada
-- anteriormente sem a coluna descricao.
ALTER TABLE public.planejamentos
    ADD COLUMN IF NOT EXISTS descricao TEXT;

CREATE TABLE IF NOT EXISTS public.planejamento_materias (
    id BIGSERIAL,
    planejamento_fk BIGINT NOT NULL,
    materia_fk BIGINT NOT NULL,
    peso SMALLINT NOT NULL DEFAULT 3,
    ordem INTEGER,
    data_finalizar_prevista DATE,
    data_finalizado DATE,
    observacoes TEXT,

    CONSTRAINT pk_planejamento_materias
        PRIMARY KEY (id),
    CONSTRAINT fk_plan_materias_planejamento
        FOREIGN KEY (planejamento_fk)
        REFERENCES public.planejamentos (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_plan_materias_materia
        FOREIGN KEY (materia_fk)
        REFERENCES public.materias (id)
        ON DELETE RESTRICT,
    CONSTRAINT uk_plan_materias_planejamento_materia
        UNIQUE (planejamento_fk, materia_fk),
    CONSTRAINT ck_plan_materias_peso
        CHECK (peso BETWEEN 1 AND 5),
    CONSTRAINT ck_plan_materias_ordem
        CHECK (ordem IS NULL OR ordem > 0)
);

CREATE TABLE IF NOT EXISTS public.planejamento_grupos_assuntos (
    id BIGSERIAL,
    planejamento_materia_fk BIGINT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    nome VARCHAR(150),
    peso SMALLINT NOT NULL DEFAULT 3,
    ordem INTEGER,
    data_prevista DATE,
    data_finalizada DATE,
    observacoes TEXT,

    CONSTRAINT pk_planejamento_grupos_assuntos
        PRIMARY KEY (id),
    CONSTRAINT fk_plan_grupos_materia
        FOREIGN KEY (planejamento_materia_fk)
        REFERENCES public.planejamento_materias (id)
        ON DELETE CASCADE,
    CONSTRAINT ck_plan_grupos_tipo
        CHECK (tipo IN ('ASSUNTO', 'BLOCO')),
    CONSTRAINT ck_plan_grupos_nome
        CHECK (
            (tipo = 'ASSUNTO' AND nome IS NULL)
            OR
            (tipo = 'BLOCO' AND nome IS NOT NULL AND BTRIM(nome) <> '')
        ),
    CONSTRAINT ck_plan_grupos_peso
        CHECK (peso BETWEEN 1 AND 5),
    CONSTRAINT ck_plan_grupos_ordem
        CHECK (ordem IS NULL OR ordem > 0)
);

CREATE TABLE IF NOT EXISTS public.planejamento_grupo_assunto_itens (
    id BIGSERIAL,
    planejamento_grupo_assunto_fk BIGINT NOT NULL,
    assunto_fk BIGINT NOT NULL,
    peso SMALLINT NOT NULL DEFAULT 3,
    ordem INTEGER,
    data_prevista DATE,
    observacoes TEXT,

    CONSTRAINT pk_planejamento_grupo_itens
        PRIMARY KEY (id),
    CONSTRAINT fk_plan_grupo_itens_grupo
        FOREIGN KEY (planejamento_grupo_assunto_fk)
        REFERENCES public.planejamento_grupos_assuntos (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_plan_grupo_itens_assunto
        FOREIGN KEY (assunto_fk)
        REFERENCES public.assuntos (id)
        ON DELETE RESTRICT,
    CONSTRAINT uk_plan_grupo_itens_grupo_assunto
        UNIQUE (planejamento_grupo_assunto_fk, assunto_fk),
    CONSTRAINT ck_plan_grupo_itens_peso
        CHECK (peso BETWEEN 1 AND 5),
    CONSTRAINT ck_plan_grupo_itens_ordem
        CHECK (ordem IS NULL OR ordem > 0)
);

CREATE TABLE IF NOT EXISTS public.planejamento_item_etapas (
    id BIGSERIAL,
    planejamento_grupo_assunto_item_fk BIGINT NOT NULL,
    tipo_registro_fk BIGINT NOT NULL,
    concluida BOOLEAN NOT NULL DEFAULT FALSE,
    concluida_manualmente BOOLEAN NOT NULL DEFAULT FALSE,
    ordem INTEGER,
    data_prevista DATE,
    data_concluida DATE,
    observacoes TEXT,

    CONSTRAINT pk_planejamento_item_etapas
        PRIMARY KEY (id),
    CONSTRAINT fk_plan_item_etapas_item
        FOREIGN KEY (planejamento_grupo_assunto_item_fk)
        REFERENCES public.planejamento_grupo_assunto_itens (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_plan_item_etapas_tipo_registro
        FOREIGN KEY (tipo_registro_fk)
        REFERENCES public.tipos_registro (id)
        ON DELETE RESTRICT,
    CONSTRAINT uk_plan_item_etapas_item_tipo
        UNIQUE (planejamento_grupo_assunto_item_fk, tipo_registro_fk),
    CONSTRAINT ck_plan_item_etapas_ordem
        CHECK (ordem IS NULL OR ordem > 0),
    CONSTRAINT ck_plan_item_etapas_conclusao
        CHECK (concluida OR NOT concluida_manualmente)
);

ALTER TABLE public.registros
    ADD COLUMN IF NOT EXISTS concluido BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS planejamento_etapa_fk BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_registros_planejamento_etapa'
          AND conrelid = 'public.registros'::regclass
    ) THEN
        ALTER TABLE public.registros
            ADD CONSTRAINT fk_registros_planejamento_etapa
            FOREIGN KEY (planejamento_etapa_fk)
            REFERENCES public.planejamento_item_etapas (id)
            ON DELETE SET NULL;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_plan_materias_planejamento
    ON public.planejamento_materias (planejamento_fk);

CREATE INDEX IF NOT EXISTS idx_plan_materias_materia
    ON public.planejamento_materias (materia_fk);

CREATE INDEX IF NOT EXISTS idx_plan_grupos_materia_ordem
    ON public.planejamento_grupos_assuntos
       (planejamento_materia_fk, ordem);

CREATE INDEX IF NOT EXISTS idx_plan_grupo_itens_grupo_ordem
    ON public.planejamento_grupo_assunto_itens
       (planejamento_grupo_assunto_fk, ordem);

CREATE INDEX IF NOT EXISTS idx_plan_grupo_itens_assunto
    ON public.planejamento_grupo_assunto_itens (assunto_fk);

CREATE INDEX IF NOT EXISTS idx_plan_item_etapas_item_ordem
    ON public.planejamento_item_etapas
       (planejamento_grupo_assunto_item_fk, ordem);

CREATE INDEX IF NOT EXISTS idx_plan_item_etapas_tipo
    ON public.planejamento_item_etapas (tipo_registro_fk);

CREATE INDEX IF NOT EXISTS idx_registros_planejamento_etapa
    ON public.registros (planejamento_etapa_fk);

-- Garante que um item use um assunto pertencente a mesma materia do grupo.
-- Tambem impede que um grupo do tipo ASSUNTO receba mais de um item.
CREATE OR REPLACE FUNCTION public.validar_planejamento_grupo_item()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_tipo_grupo VARCHAR(10);
    v_materia_grupo BIGINT;
    v_materia_assunto BIGINT;
BEGIN
    SELECT pga.tipo, pm.materia_fk
      INTO v_tipo_grupo, v_materia_grupo
      FROM public.planejamento_grupos_assuntos pga
      JOIN public.planejamento_materias pm
        ON pm.id = pga.planejamento_materia_fk
     WHERE pga.id = NEW.planejamento_grupo_assunto_fk;

    SELECT a.materia_fk
      INTO v_materia_assunto
      FROM public.assuntos a
     WHERE a.id = NEW.assunto_fk;

    IF v_materia_grupo IS DISTINCT FROM v_materia_assunto THEN
        RAISE EXCEPTION
            'O assunto % nao pertence a materia % do grupo',
            NEW.assunto_fk,
            v_materia_grupo;
    END IF;

    IF v_tipo_grupo = 'ASSUNTO' AND EXISTS (
        SELECT 1
          FROM public.planejamento_grupo_assunto_itens pgai
         WHERE pgai.planejamento_grupo_assunto_fk =
               NEW.planejamento_grupo_assunto_fk
           AND pgai.id <> COALESCE(NEW.id, -1)
    ) THEN
        RAISE EXCEPTION
            'Um grupo do tipo ASSUNTO pode possuir somente um item';
    END IF;

    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_validar_plan_grupo_item
    ON public.planejamento_grupo_assunto_itens;

CREATE TRIGGER trg_validar_plan_grupo_item
BEFORE INSERT OR UPDATE OF planejamento_grupo_assunto_fk, assunto_fk
ON public.planejamento_grupo_assunto_itens
FOR EACH ROW
EXECUTE FUNCTION public.validar_planejamento_grupo_item();

-- Impede que um bloco com varios itens seja convertido diretamente em ASSUNTO.
CREATE OR REPLACE FUNCTION public.validar_tipo_planejamento_grupo()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.tipo = 'ASSUNTO' AND (
        SELECT COUNT(*)
          FROM public.planejamento_grupo_assunto_itens pgai
         WHERE pgai.planejamento_grupo_assunto_fk = NEW.id
    ) > 1 THEN
        RAISE EXCEPTION
            'Um grupo do tipo ASSUNTO pode possuir somente um item';
    END IF;

    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_validar_tipo_plan_grupo
    ON public.planejamento_grupos_assuntos;

CREATE TRIGGER trg_validar_tipo_plan_grupo
BEFORE UPDATE OF tipo
ON public.planejamento_grupos_assuntos
FOR EACH ROW
EXECUTE FUNCTION public.validar_tipo_planejamento_grupo();

COMMIT;
