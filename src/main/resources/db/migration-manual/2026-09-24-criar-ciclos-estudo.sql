CREATE TABLE IF NOT EXISTS public.ciclos_estudo (
    id serial4 NOT NULL,
    nome varchar(150) NOT NULL,
    CONSTRAINT ciclos_estudo_pkey PRIMARY KEY (id),
    CONSTRAINT uk_ciclos_estudo_nome UNIQUE (nome)
);

ALTER TABLE public.registros
    ADD COLUMN IF NOT EXISTS ciclo_fk BIGINT;

ALTER TABLE public.registros
    DROP CONSTRAINT IF EXISTS registros_planejamento_fk_fkey;

ALTER TABLE public.registros
    DROP CONSTRAINT IF EXISTS fk_registros_ciclo;

ALTER TABLE public.registros
    ADD CONSTRAINT fk_registros_ciclo
    FOREIGN KEY (ciclo_fk)
    REFERENCES public.ciclos_estudo(id);

CREATE INDEX IF NOT EXISTS idx_registros_ciclo_data
ON public.registros (ciclo_fk, data_estudo DESC);
