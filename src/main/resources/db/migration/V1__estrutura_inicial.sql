-- Baseline do schema público da aplicação em 2026-11-28.
-- Não altere esta migration depois que ela for aplicada.

CREATE FUNCTION public.validar_planejamento_grupo_item() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    v_tipo_grupo VARCHAR(10);
    v_materia_grupo BIGINT;
    v_materia_assunto BIGINT;
BEGIN
    SELECT pga.tipo, pm.materia_fk
      INTO v_tipo_grupo, v_materia_grupo
      FROM public.planejamento_grupos_assuntos pga
      JOIN public.planejamento_materias pm
        ON pm.id = pga.planejamento_materia_fk
     WHERE pga.id = NEW.planejamento_grupo_assunto_fk;

    SELECT a.materia_fk
      INTO v_materia_assunto
      FROM public.assuntos a
     WHERE a.id = NEW.assunto_fk;

    IF v_materia_grupo IS DISTINCT FROM v_materia_assunto THEN
        RAISE EXCEPTION
            'O assunto % nao pertence a materia % do grupo',
            NEW.assunto_fk,
            v_materia_grupo;
    END IF;

    IF v_tipo_grupo = 'ASSUNTO' AND EXISTS (
        SELECT 1
          FROM public.planejamento_grupo_assunto_itens pgai
         WHERE pgai.planejamento_grupo_assunto_fk =
               NEW.planejamento_grupo_assunto_fk
           AND pgai.id <> COALESCE(NEW.id, -1)
    ) THEN
        RAISE EXCEPTION
            'Um grupo do tipo ASSUNTO pode possuir somente um item';
    END IF;

    RETURN NEW;
END;
$$;

CREATE FUNCTION public.validar_tipo_planejamento_grupo() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.tipo = 'ASSUNTO' AND (
        SELECT COUNT(*)
          FROM public.planejamento_grupo_assunto_itens pgai
         WHERE pgai.planejamento_grupo_assunto_fk = NEW.id
    ) > 1 THEN
        RAISE EXCEPTION
            'Um grupo do tipo ASSUNTO pode possuir somente um item';
    END IF;

    RETURN NEW;
END;
$$;

CREATE TABLE public.assuntos (
    id integer NOT NULL,
    materia_fk integer NOT NULL,
    nome character varying(150) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);

CREATE SEQUENCE public.assuntos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.assuntos_id_seq OWNED BY public.assuntos.id;

CREATE TABLE public.categorias (
    id integer NOT NULL,
    nome character varying(100) NOT NULL
);

CREATE SEQUENCE public.categorias_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.categorias_id_seq OWNED BY public.categorias.id;

CREATE TABLE public.ciclo_itens (
    id bigint NOT NULL,
    ciclo_fk bigint NOT NULL,
    materia_fk bigint,
    ordem integer NOT NULL,
    ativo boolean DEFAULT true NOT NULL,
    planejamento_grupo_materia_fk bigint,
    registro_conclusao_fk bigint,
    CONSTRAINT ck_ciclo_itens_tipo CHECK ((num_nonnulls(materia_fk, planejamento_grupo_materia_fk) = 1))
);

CREATE SEQUENCE public.ciclo_materias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.ciclo_materias_id_seq OWNED BY public.ciclo_itens.id;

CREATE TABLE public.ciclos (
    id bigint NOT NULL,
    nome character varying(150) NOT NULL,
    ativo boolean DEFAULT true NOT NULL,
    observacoes text,
    criado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE public.materiais_guia (
    id integer NOT NULL,
    nome character varying(150) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);

CREATE SEQUENCE public.ciclos_estudo_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.ciclos_estudo_id_seq OWNED BY public.materiais_guia.id;

CREATE SEQUENCE public.ciclos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.ciclos_id_seq OWNED BY public.ciclos.id;

CREATE TABLE public.materia_categorias (
    materia_id integer NOT NULL,
    categoria_id integer NOT NULL
);

CREATE TABLE public.material_tipos (
    id integer NOT NULL,
    nome character varying(50) NOT NULL
);

CREATE SEQUENCE public.material_tipos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.material_tipos_id_seq OWNED BY public.material_tipos.id;

CREATE TABLE public.materias (
    id integer NOT NULL,
    nome character varying(100) NOT NULL,
    categorias character varying(255),
    ativo boolean DEFAULT true NOT NULL
);

CREATE SEQUENCE public.materias_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.materias_id_seq OWNED BY public.materias.id;

CREATE TABLE public.metas_diarias (
    id bigint NOT NULL,
    data date NOT NULL,
    meta_minutos integer NOT NULL,
    CONSTRAINT ck_metas_diarias_meta_minutos CHECK ((meta_minutos > 0))
);

CREATE SEQUENCE public.metas_diarias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.metas_diarias_id_seq OWNED BY public.metas_diarias.id;

CREATE TABLE public.metas_materias (
    id bigint NOT NULL,
    materia_fk bigint NOT NULL,
    meta_semanal_minutos integer NOT NULL,
    CONSTRAINT ck_metas_materias_meta_semanal CHECK ((meta_semanal_minutos > 0))
);

CREATE SEQUENCE public.metas_materias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.metas_materias_id_seq OWNED BY public.metas_materias.id;

CREATE TABLE public.metas_semanais (
    id bigint NOT NULL,
    inicio_semana date NOT NULL,
    meta_minutos integer NOT NULL,
    CONSTRAINT ck_metas_semanais_inicio_segunda CHECK ((EXTRACT(dow FROM inicio_semana) = (1)::numeric)),
    CONSTRAINT ck_metas_semanais_meta_minutos CHECK ((meta_minutos > 0))
);

CREATE SEQUENCE public.metas_semanais_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.metas_semanais_id_seq OWNED BY public.metas_semanais.id;

CREATE TABLE public.planejamento_grupo_assunto_itens (
    id bigint NOT NULL,
    planejamento_grupo_assunto_fk bigint NOT NULL,
    assunto_fk bigint NOT NULL,
    peso smallint DEFAULT 3 NOT NULL,
    ordem integer,
    data_prevista date,
    observacoes text,
    dificuldade smallint,
    CONSTRAINT ck_plan_grupo_itens_dificuldade CHECK (((dificuldade IS NULL) OR ((dificuldade >= 1) AND (dificuldade <= 5)))),
    CONSTRAINT ck_plan_grupo_itens_ordem CHECK (((ordem IS NULL) OR (ordem > 0))),
    CONSTRAINT ck_plan_grupo_itens_peso CHECK (((peso >= 1) AND (peso <= 5)))
);

CREATE SEQUENCE public.planejamento_grupo_assunto_itens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.planejamento_grupo_assunto_itens_id_seq OWNED BY public.planejamento_grupo_assunto_itens.id;

CREATE TABLE public.planejamento_grupos_assuntos (
    id bigint NOT NULL,
    planejamento_materia_fk bigint NOT NULL,
    tipo character varying(10) NOT NULL,
    nome character varying(150),
    peso smallint DEFAULT 3 NOT NULL,
    ordem integer,
    data_prevista date,
    data_finalizada date,
    observacoes text,
    CONSTRAINT ck_plan_grupos_nome CHECK (((((tipo)::text = 'ASSUNTO'::text) AND (nome IS NULL)) OR (((tipo)::text = 'BLOCO'::text) AND (nome IS NOT NULL) AND (btrim((nome)::text) <> ''::text)))),
    CONSTRAINT ck_plan_grupos_ordem CHECK (((ordem IS NULL) OR (ordem > 0))),
    CONSTRAINT ck_plan_grupos_peso CHECK (((peso >= 1) AND (peso <= 5))),
    CONSTRAINT ck_plan_grupos_tipo CHECK (((tipo)::text = ANY ((ARRAY['ASSUNTO'::character varying, 'BLOCO'::character varying])::text[])))
);

CREATE SEQUENCE public.planejamento_grupos_assuntos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.planejamento_grupos_assuntos_id_seq OWNED BY public.planejamento_grupos_assuntos.id;

CREATE TABLE public.planejamento_grupos_materias (
    id bigint NOT NULL,
    planejamento_fk bigint NOT NULL,
    nome character varying(150) NOT NULL,
    peso smallint DEFAULT 3 NOT NULL,
    ordem integer,
    data_prevista date,
    data_finalizada date,
    observacoes text,
    dificuldade smallint,
    CONSTRAINT ck_plan_grupos_materias_dificuldade CHECK (((dificuldade IS NULL) OR ((dificuldade >= 1) AND (dificuldade <= 5))))
);

CREATE SEQUENCE public.planejamento_grupos_materias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.planejamento_grupos_materias_id_seq OWNED BY public.planejamento_grupos_materias.id;

CREATE TABLE public.planejamento_item_etapas (
    id bigint NOT NULL,
    planejamento_grupo_assunto_item_fk bigint NOT NULL,
    tipo_registro_fk bigint NOT NULL,
    concluida boolean DEFAULT false NOT NULL,
    concluida_manualmente boolean DEFAULT false NOT NULL,
    ordem integer,
    data_prevista date,
    data_concluida date,
    observacoes text,
    CONSTRAINT ck_plan_item_etapas_conclusao CHECK ((concluida OR (NOT concluida_manualmente))),
    CONSTRAINT ck_plan_item_etapas_ordem CHECK (((ordem IS NULL) OR (ordem > 0)))
);

CREATE SEQUENCE public.planejamento_item_etapas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.planejamento_item_etapas_id_seq OWNED BY public.planejamento_item_etapas.id;

CREATE TABLE public.planejamento_materias (
    id bigint NOT NULL,
    planejamento_fk bigint NOT NULL,
    materia_fk bigint NOT NULL,
    peso smallint DEFAULT 3 NOT NULL,
    ordem integer,
    data_finalizar_prevista date,
    data_finalizado date,
    observacoes text,
    planejamento_grupo_materia_fk bigint,
    dificuldade smallint,
    CONSTRAINT ck_plan_materias_dificuldade CHECK (((dificuldade IS NULL) OR ((dificuldade >= 1) AND (dificuldade <= 5)))),
    CONSTRAINT ck_plan_materias_ordem CHECK (((ordem IS NULL) OR (ordem > 0))),
    CONSTRAINT ck_plan_materias_peso CHECK (((peso >= 1) AND (peso <= 5)))
);

CREATE SEQUENCE public.planejamento_materias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.planejamento_materias_id_seq OWNED BY public.planejamento_materias.id;

CREATE TABLE public.planejamentos (
    id bigint NOT NULL,
    nome character varying(150) NOT NULL,
    descricao text,
    data_fim_prevista date,
    ativo boolean DEFAULT true NOT NULL
);

CREATE SEQUENCE public.planejamentos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.planejamentos_id_seq OWNED BY public.planejamentos.id;




CREATE TABLE public.registros (
    id integer NOT NULL,
    planejamento_fk integer,
    materia_fk integer NOT NULL,
    assunto_fk integer NOT NULL,
    material_tipo_fk integer,
    material_nome character varying(255),
    ponto_parada character varying(255),
    questoes_feitas integer DEFAULT 0,
    questoes_acertadas integer DEFAULT 0,
    revisao_fk_assunto integer,
    revisao_complemento text,
    data_estudo date DEFAULT CURRENT_DATE NOT NULL,
    tempo_estudado time without time zone NOT NULL,
    link_documento text,
    observacoes text,
    questoes_revisao_feitas integer DEFAULT 0,
    questoes_revisao_acertadas integer DEFAULT 0,
    tipo_registro_fk bigint,
    material_guia_fk bigint,
    concluido boolean DEFAULT false NOT NULL,
    planejamento_etapa_fk bigint,
    horario_inicio time without time zone
);

CREATE SEQUENCE public.registros_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.registros_id_seq OWNED BY public.registros.id;

CREATE TABLE public.tipos_registro (
    id bigint NOT NULL,
    nome character varying(50) NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);

CREATE SEQUENCE public.tipos_registro_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.tipos_registro_id_seq OWNED BY public.tipos_registro.id;

ALTER TABLE ONLY public.assuntos ALTER COLUMN id SET DEFAULT nextval('public.assuntos_id_seq'::regclass);

ALTER TABLE ONLY public.categorias ALTER COLUMN id SET DEFAULT nextval('public.categorias_id_seq'::regclass);

ALTER TABLE ONLY public.ciclo_itens ALTER COLUMN id SET DEFAULT nextval('public.ciclo_materias_id_seq'::regclass);

ALTER TABLE ONLY public.ciclos ALTER COLUMN id SET DEFAULT nextval('public.ciclos_id_seq'::regclass);

ALTER TABLE ONLY public.materiais_guia ALTER COLUMN id SET DEFAULT nextval('public.ciclos_estudo_id_seq'::regclass);

ALTER TABLE ONLY public.material_tipos ALTER COLUMN id SET DEFAULT nextval('public.material_tipos_id_seq'::regclass);

ALTER TABLE ONLY public.materias ALTER COLUMN id SET DEFAULT nextval('public.materias_id_seq'::regclass);

ALTER TABLE ONLY public.metas_diarias ALTER COLUMN id SET DEFAULT nextval('public.metas_diarias_id_seq'::regclass);

ALTER TABLE ONLY public.metas_materias ALTER COLUMN id SET DEFAULT nextval('public.metas_materias_id_seq'::regclass);

ALTER TABLE ONLY public.metas_semanais ALTER COLUMN id SET DEFAULT nextval('public.metas_semanais_id_seq'::regclass);

ALTER TABLE ONLY public.planejamento_grupo_assunto_itens ALTER COLUMN id SET DEFAULT nextval('public.planejamento_grupo_assunto_itens_id_seq'::regclass);

ALTER TABLE ONLY public.planejamento_grupos_assuntos ALTER COLUMN id SET DEFAULT nextval('public.planejamento_grupos_assuntos_id_seq'::regclass);

ALTER TABLE ONLY public.planejamento_grupos_materias ALTER COLUMN id SET DEFAULT nextval('public.planejamento_grupos_materias_id_seq'::regclass);

ALTER TABLE ONLY public.planejamento_item_etapas ALTER COLUMN id SET DEFAULT nextval('public.planejamento_item_etapas_id_seq'::regclass);

ALTER TABLE ONLY public.planejamento_materias ALTER COLUMN id SET DEFAULT nextval('public.planejamento_materias_id_seq'::regclass);

ALTER TABLE ONLY public.planejamentos ALTER COLUMN id SET DEFAULT nextval('public.planejamentos_id_seq'::regclass);

ALTER TABLE ONLY public.registros ALTER COLUMN id SET DEFAULT nextval('public.registros_id_seq'::regclass);

ALTER TABLE ONLY public.tipos_registro ALTER COLUMN id SET DEFAULT nextval('public.tipos_registro_id_seq'::regclass);

ALTER TABLE ONLY public.assuntos
    ADD CONSTRAINT assuntos_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_nome_key UNIQUE (nome);

ALTER TABLE ONLY public.categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.ciclo_itens
    ADD CONSTRAINT ciclo_materias_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.ciclos
    ADD CONSTRAINT ciclos_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.materia_categorias
    ADD CONSTRAINT materia_categorias_pkey PRIMARY KEY (materia_id, categoria_id);

ALTER TABLE ONLY public.materiais_guia
    ADD CONSTRAINT materiais_guia_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.material_tipos
    ADD CONSTRAINT material_tipos_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.materias
    ADD CONSTRAINT materias_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.metas_diarias
    ADD CONSTRAINT metas_diarias_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.metas_materias
    ADD CONSTRAINT metas_materias_materia_fk_key UNIQUE (materia_fk);

ALTER TABLE ONLY public.metas_materias
    ADD CONSTRAINT metas_materias_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.metas_semanais
    ADD CONSTRAINT metas_semanais_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.planejamento_grupo_assunto_itens
    ADD CONSTRAINT pk_planejamento_grupo_itens PRIMARY KEY (id);

ALTER TABLE ONLY public.planejamento_grupos_assuntos
    ADD CONSTRAINT pk_planejamento_grupos_assuntos PRIMARY KEY (id);

ALTER TABLE ONLY public.planejamento_item_etapas
    ADD CONSTRAINT pk_planejamento_item_etapas PRIMARY KEY (id);

ALTER TABLE ONLY public.planejamento_materias
    ADD CONSTRAINT pk_planejamento_materias PRIMARY KEY (id);

ALTER TABLE ONLY public.planejamentos
    ADD CONSTRAINT pk_planejamentos PRIMARY KEY (id);

ALTER TABLE ONLY public.planejamento_grupos_materias
    ADD CONSTRAINT planejamento_grupos_materias_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT registros_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.tipos_registro
    ADD CONSTRAINT tipos_registro_nome_key UNIQUE (nome);

ALTER TABLE ONLY public.tipos_registro
    ADD CONSTRAINT tipos_registro_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.ciclo_itens
    ADD CONSTRAINT uk_ciclo_itens_grupo UNIQUE (ciclo_fk, planejamento_grupo_materia_fk);

ALTER TABLE ONLY public.ciclo_itens
    ADD CONSTRAINT uk_ciclo_itens_ordem UNIQUE (ciclo_fk, ordem);

ALTER TABLE ONLY public.materiais_guia
    ADD CONSTRAINT uk_materiais_guia_nome UNIQUE (nome);

ALTER TABLE ONLY public.metas_diarias
    ADD CONSTRAINT uk_metas_diarias_data UNIQUE (data);

ALTER TABLE ONLY public.metas_semanais
    ADD CONSTRAINT uk_metas_semanais_inicio_semana UNIQUE (inicio_semana);

ALTER TABLE ONLY public.planejamento_grupo_assunto_itens
    ADD CONSTRAINT uk_plan_grupo_itens_grupo_assunto UNIQUE (planejamento_grupo_assunto_fk, assunto_fk);

ALTER TABLE ONLY public.planejamento_item_etapas
    ADD CONSTRAINT uk_plan_item_etapas_item_tipo UNIQUE (planejamento_grupo_assunto_item_fk, tipo_registro_fk);

CREATE INDEX idx_assuntos_materia_ativo_nome ON public.assuntos USING btree (materia_fk, ativo, nome);

CREATE INDEX idx_ciclo_itens_ciclo_ordem ON public.ciclo_itens USING btree (ciclo_fk, ordem);

CREATE INDEX idx_ciclo_itens_registro_conclusao ON public.ciclo_itens USING btree (registro_conclusao_fk);

CREATE INDEX idx_materia_categorias_categoria_id ON public.materia_categorias USING btree (categoria_id);

CREATE INDEX idx_materiais_guia_ativo_nome ON public.materiais_guia USING btree (ativo, nome);

CREATE INDEX idx_materias_ativo_nome ON public.materias USING btree (ativo, nome);

CREATE INDEX idx_plan_grupo_itens_assunto ON public.planejamento_grupo_assunto_itens USING btree (assunto_fk);

CREATE INDEX idx_plan_grupo_itens_grupo_ordem ON public.planejamento_grupo_assunto_itens USING btree (planejamento_grupo_assunto_fk, ordem);

CREATE INDEX idx_plan_grupos_materia_ordem ON public.planejamento_grupos_assuntos USING btree (planejamento_materia_fk, ordem);

CREATE INDEX idx_plan_item_etapas_item_ordem ON public.planejamento_item_etapas USING btree (planejamento_grupo_assunto_item_fk, ordem);

CREATE INDEX idx_plan_item_etapas_tipo ON public.planejamento_item_etapas USING btree (tipo_registro_fk);

CREATE INDEX idx_plan_materias_materia ON public.planejamento_materias USING btree (materia_fk);

CREATE INDEX idx_plan_materias_planejamento ON public.planejamento_materias USING btree (planejamento_fk);

CREATE INDEX idx_planejamento_grupos_materias_planejamento_ordem ON public.planejamento_grupos_materias USING btree (planejamento_fk, ordem);

CREATE INDEX idx_planejamento_materias_grupo_materia ON public.planejamento_materias USING btree (planejamento_grupo_materia_fk);

CREATE INDEX idx_planejamentos_ativo_nome ON public.planejamentos USING btree (ativo, nome);

CREATE INDEX idx_registros_data_id_desc ON public.registros USING btree (data_estudo DESC, id DESC);

CREATE INDEX idx_registros_materia_data_estudo ON public.registros USING btree (materia_fk, data_estudo);

CREATE INDEX idx_registros_materia_data_id_desc ON public.registros USING btree (materia_fk, data_estudo DESC, id DESC);

CREATE INDEX idx_registros_material_guia_data ON public.registros USING btree (material_guia_fk, data_estudo DESC);

CREATE INDEX idx_registros_planejamento_data ON public.registros USING btree (planejamento_fk, data_estudo DESC);

CREATE INDEX idx_registros_planejamento_etapa ON public.registros USING btree (planejamento_etapa_fk);

CREATE INDEX idx_registros_tipo_registro_data ON public.registros USING btree (tipo_registro_fk, data_estudo DESC);

CREATE INDEX idx_tipos_registro_ativo_nome ON public.tipos_registro USING btree (ativo, nome);

CREATE UNIQUE INDEX uk_planejamentos_nome_ativo ON public.planejamentos USING btree (nome) WHERE (ativo = true);

CREATE TRIGGER trg_validar_tipo_plan_grupo BEFORE UPDATE OF tipo ON public.planejamento_grupos_assuntos FOR EACH ROW EXECUTE FUNCTION public.validar_tipo_planejamento_grupo();

ALTER TABLE ONLY public.assuntos
    ADD CONSTRAINT assuntos_materia_fk_fkey FOREIGN KEY (materia_fk) REFERENCES public.materias(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.ciclo_itens
    ADD CONSTRAINT fk_ciclo_itens_grupo FOREIGN KEY (planejamento_grupo_materia_fk) REFERENCES public.planejamento_grupos_materias(id);

ALTER TABLE ONLY public.ciclo_itens
    ADD CONSTRAINT fk_ciclo_itens_registro_conclusao FOREIGN KEY (registro_conclusao_fk) REFERENCES public.registros(id) ON DELETE SET NULL;

ALTER TABLE ONLY public.ciclo_itens
    ADD CONSTRAINT fk_ciclo_materias_ciclo FOREIGN KEY (ciclo_fk) REFERENCES public.ciclos(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.ciclo_itens
    ADD CONSTRAINT fk_ciclo_materias_materia FOREIGN KEY (materia_fk) REFERENCES public.materias(id);

ALTER TABLE ONLY public.metas_materias
    ADD CONSTRAINT fk_metas_materias_materia FOREIGN KEY (materia_fk) REFERENCES public.materias(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.planejamento_grupo_assunto_itens
    ADD CONSTRAINT fk_plan_grupo_itens_assunto FOREIGN KEY (assunto_fk) REFERENCES public.assuntos(id) ON DELETE RESTRICT;

ALTER TABLE ONLY public.planejamento_grupo_assunto_itens
    ADD CONSTRAINT fk_plan_grupo_itens_grupo FOREIGN KEY (planejamento_grupo_assunto_fk) REFERENCES public.planejamento_grupos_assuntos(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.planejamento_grupos_assuntos
    ADD CONSTRAINT fk_plan_grupos_materia FOREIGN KEY (planejamento_materia_fk) REFERENCES public.planejamento_materias(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.planejamento_item_etapas
    ADD CONSTRAINT fk_plan_item_etapas_item FOREIGN KEY (planejamento_grupo_assunto_item_fk) REFERENCES public.planejamento_grupo_assunto_itens(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.planejamento_item_etapas
    ADD CONSTRAINT fk_plan_item_etapas_tipo_registro FOREIGN KEY (tipo_registro_fk) REFERENCES public.tipos_registro(id) ON DELETE RESTRICT;

ALTER TABLE ONLY public.planejamento_materias
    ADD CONSTRAINT fk_plan_materias_materia FOREIGN KEY (materia_fk) REFERENCES public.materias(id) ON DELETE RESTRICT;

ALTER TABLE ONLY public.planejamento_materias
    ADD CONSTRAINT fk_plan_materias_planejamento FOREIGN KEY (planejamento_fk) REFERENCES public.planejamentos(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.planejamento_grupos_materias
    ADD CONSTRAINT fk_planejamento_grupos_materias_planejamento FOREIGN KEY (planejamento_fk) REFERENCES public.planejamentos(id);

ALTER TABLE ONLY public.planejamento_materias
    ADD CONSTRAINT fk_planejamento_materias_grupo_materia FOREIGN KEY (planejamento_grupo_materia_fk) REFERENCES public.planejamento_grupos_materias(id);

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT fk_registros_material_guia FOREIGN KEY (material_guia_fk) REFERENCES public.materiais_guia(id);

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT fk_registros_planejamento FOREIGN KEY (planejamento_fk) REFERENCES public.planejamentos(id) ON DELETE SET NULL;

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT fk_registros_planejamento_etapa FOREIGN KEY (planejamento_etapa_fk) REFERENCES public.planejamento_item_etapas(id) ON DELETE SET NULL;

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT fk_registros_tipo_registro FOREIGN KEY (tipo_registro_fk) REFERENCES public.tipos_registro(id);

ALTER TABLE ONLY public.materia_categorias
    ADD CONSTRAINT materia_categorias_categoria_id_fkey FOREIGN KEY (categoria_id) REFERENCES public.categorias(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.materia_categorias
    ADD CONSTRAINT materia_categorias_materia_id_fkey FOREIGN KEY (materia_id) REFERENCES public.materias(id) ON DELETE CASCADE;

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT registros_assunto_fk_fkey FOREIGN KEY (assunto_fk) REFERENCES public.assuntos(id);

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT registros_materia_fk_fkey FOREIGN KEY (materia_fk) REFERENCES public.materias(id);

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT registros_material_tipo_fk_fkey FOREIGN KEY (material_tipo_fk) REFERENCES public.material_tipos(id);

ALTER TABLE ONLY public.registros
    ADD CONSTRAINT registros_revisao_fk_assunto_fkey FOREIGN KEY (revisao_fk_assunto) REFERENCES public.assuntos(id);


-- Dados de referência iniciais
INSERT INTO public.tipos_registro (nome, ativo)
VALUES
    ('Assunto', true),
    ('Questões', true),
    ('Resumo', true),
    ('Simulado', true),
    ('Prova', true)
ON CONFLICT (nome) DO NOTHING;
