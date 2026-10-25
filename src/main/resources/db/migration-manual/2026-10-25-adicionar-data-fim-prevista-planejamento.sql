ALTER TABLE public.planejamentos
    ADD COLUMN IF NOT EXISTS data_fim_prevista DATE;
