BEGIN;

-- O cadastro antes chamado de ciclo representa, na verdade, o material ou
-- pacote que guia o estudo (por exemplo, "SEFAZ SC Estratégia").
ALTER TABLE IF EXISTS public.ciclos_estudo
    RENAME TO materiais_guia;

ALTER TABLE IF EXISTS public.materiais_guia
    RENAME CONSTRAINT ciclos_estudo_pkey TO materiais_guia_pkey;

ALTER TABLE IF EXISTS public.materiais_guia
    RENAME CONSTRAINT uk_ciclos_estudo_nome TO uk_materiais_guia_nome;

ALTER TABLE public.registros
    DROP CONSTRAINT IF EXISTS fk_registros_ciclo;

ALTER TABLE public.registros
    RENAME COLUMN ciclo_fk TO material_guia_fk;

ALTER TABLE public.registros
    ADD CONSTRAINT fk_registros_material_guia
    FOREIGN KEY (material_guia_fk)
    REFERENCES public.materiais_guia (id);

ALTER INDEX IF EXISTS public.idx_registros_ciclo_data
    RENAME TO idx_registros_material_guia_data;

COMMIT;
