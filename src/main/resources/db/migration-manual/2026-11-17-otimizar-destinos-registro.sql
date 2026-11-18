CREATE INDEX IF NOT EXISTS idx_plan_materias_planejamento_materia
    ON public.planejamento_materias (planejamento_fk, materia_fk);
