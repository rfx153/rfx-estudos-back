BEGIN;

CREATE TABLE public.metas_diarias (
    id BIGSERIAL PRIMARY KEY,
    data DATE NOT NULL,
    meta_minutos INTEGER NOT NULL,
    CONSTRAINT uk_metas_diarias_data UNIQUE (data),
    CONSTRAINT ck_metas_diarias_meta_minutos CHECK (meta_minutos > 0)
);

COMMIT;
