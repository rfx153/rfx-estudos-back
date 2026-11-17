ALTER TABLE public.materias
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE public.assuntos
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE public.materiais_guia
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE public.tipos_registro
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;

CREATE INDEX IF NOT EXISTS idx_materias_ativo_nome
    ON public.materias (ativo, nome);

CREATE INDEX IF NOT EXISTS idx_assuntos_materia_ativo_nome
    ON public.assuntos (materia_fk, ativo, nome);

CREATE INDEX IF NOT EXISTS idx_materiais_guia_ativo_nome
    ON public.materiais_guia (ativo, nome);

CREATE INDEX IF NOT EXISTS idx_tipos_registro_ativo_nome
    ON public.tipos_registro (ativo, nome);
