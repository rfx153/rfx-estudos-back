ALTER TABLE public.planejamentos
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE public.planejamentos
    DROP CONSTRAINT IF EXISTS uk_planejamentos_nome;

ALTER TABLE public.planejamentos
    DROP CONSTRAINT IF EXISTS planejamentos_nome_key;

CREATE UNIQUE INDEX IF NOT EXISTS uk_planejamentos_nome_ativo
    ON public.planejamentos (nome)
    WHERE ativo = TRUE;

CREATE INDEX IF NOT EXISTS idx_planejamentos_ativo_nome
    ON public.planejamentos (ativo, nome);
