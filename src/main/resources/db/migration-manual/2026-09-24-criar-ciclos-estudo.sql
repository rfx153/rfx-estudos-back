CREATE TABLE IF NOT EXISTS public.ciclos_estudo (
    id serial4 NOT NULL,
    nome varchar(150) NOT NULL,
    CONSTRAINT ciclos_estudo_pkey PRIMARY KEY (id),
    CONSTRAINT uk_ciclos_estudo_nome UNIQUE (nome)
);

-- O campo continua se chamando planejamento_fk em registros para evitar uma
-- migração maior no front agora, mas a referência passa a ser o cadastro simples
-- de ciclos. Execute estes ALTERs apenas se registros.planejamento_fk tiver FK
-- apontando para public.planejamentos.
ALTER TABLE public.registros
    DROP CONSTRAINT IF EXISTS registros_planejamento_fk_fkey;

ALTER TABLE public.registros
    ADD CONSTRAINT registros_planejamento_fk_fkey
    FOREIGN KEY (planejamento_fk)
    REFERENCES public.ciclos_estudo(id);
