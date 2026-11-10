BEGIN;

ALTER TABLE public.ciclo_itens
    ADD COLUMN registro_conclusao_fk BIGINT;

ALTER TABLE public.ciclo_itens
    ADD CONSTRAINT fk_ciclo_itens_registro_conclusao
        FOREIGN KEY (registro_conclusao_fk)
        REFERENCES public.registros (id)
        ON DELETE SET NULL;

CREATE INDEX idx_ciclo_itens_registro_conclusao
    ON public.ciclo_itens (registro_conclusao_fk);

COMMIT;
