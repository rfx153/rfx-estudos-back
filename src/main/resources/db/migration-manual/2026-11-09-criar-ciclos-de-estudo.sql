BEGIN;

CREATE TABLE IF NOT EXISTS public.ciclos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    observacoes TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.ciclo_materias (
    id BIGSERIAL PRIMARY KEY,
    ciclo_fk BIGINT NOT NULL,
    materia_fk BIGINT NOT NULL,
    ordem INTEGER NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_ciclo_materias_ciclo
        FOREIGN KEY (ciclo_fk) REFERENCES public.ciclos (id) ON DELETE CASCADE,
    CONSTRAINT fk_ciclo_materias_materia
        FOREIGN KEY (materia_fk) REFERENCES public.materias (id),
    CONSTRAINT uk_ciclo_materias_materia UNIQUE (ciclo_fk, materia_fk),
    CONSTRAINT uk_ciclo_materias_ordem UNIQUE (ciclo_fk, ordem)
);

CREATE INDEX IF NOT EXISTS idx_ciclo_materias_ciclo_ordem
    ON public.ciclo_materias (ciclo_fk, ordem);

COMMIT;
