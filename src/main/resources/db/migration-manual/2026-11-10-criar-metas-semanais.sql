BEGIN;

CREATE TABLE public.metas_semanais (
    id BIGSERIAL PRIMARY KEY,
    inicio_semana DATE NOT NULL,
    meta_minutos INTEGER NOT NULL,
    CONSTRAINT uk_metas_semanais_inicio_semana UNIQUE (inicio_semana),
    CONSTRAINT ck_metas_semanais_meta_minutos CHECK (meta_minutos > 0),
    CONSTRAINT ck_metas_semanais_inicio_segunda
        CHECK (EXTRACT(DOW FROM inicio_semana) = 1)
);

COMMIT;
