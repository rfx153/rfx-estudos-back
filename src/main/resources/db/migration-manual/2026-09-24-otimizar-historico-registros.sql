CREATE INDEX IF NOT EXISTS idx_registros_data_id_desc
    ON public.registros USING btree (data_estudo DESC, id DESC);

CREATE INDEX IF NOT EXISTS idx_registros_materia_data_id_desc
    ON public.registros USING btree (materia_fk, data_estudo DESC, id DESC);
