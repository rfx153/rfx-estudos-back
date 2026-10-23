# Proposta do módulo de planejamento

> Documento de discussão. O modelo abaixo ainda não foi implementado e pode ser refinado.

## Objetivo

Permitir a criação de planejamentos, como `SEFAZ SC`, organizados em blocos. Cada bloco poderá reunir assuntos de matérias diferentes, e cada assunto terá etapas configuráveis com base nos tipos de registro.

Exemplo:

```text
SEFAZ SC
└── Bloco 1
    ├── Português → Crase
    │   ├── Assunto
    │   ├── Resumo
    │   └── Questões
    ├── Direito Tributário → Obrigação tributária
    └── Contabilidade → Patrimônio líquido
```

## Estrutura proposta

```text
planejamentos
├── planejamento_materias
└── planejamento_blocos
    └── planejamento_bloco_assuntos
        └── planejamento_assunto_etapas
            └── registros
```

Uma linha nunca armazenará uma lista de IDs. Várias matérias, assuntos ou registros serão representados por várias linhas relacionadas.

## Planejamentos

Representa o planejamento global.

```text
planejamentos
-------------
id              BIGSERIAL PRIMARY KEY
nome            VARCHAR(150) NOT NULL UNIQUE
```

## Matérias do planejamento

Registra as matérias que participam do planejamento e permite manter prazo e observações gerais por matéria.

```text
planejamento_materias
---------------------
id                       BIGSERIAL PRIMARY KEY
planejamento_fk          BIGINT NOT NULL
materia_fk               BIGINT NOT NULL
data_finalizar_prevista  DATE
data_finalizado          DATE
observacoes              TEXT

UNIQUE (planejamento_fk, materia_fk)
```

## Blocos do planejamento

Um bloco organiza assuntos, inclusive de matérias diferentes.

```text
planejamento_blocos
-------------------
id                  BIGSERIAL PRIMARY KEY
planejamento_fk     BIGINT NOT NULL
nome                VARCHAR(150) NOT NULL
ordem               INTEGER
data_prevista       DATE
data_finalizada     DATE
observacoes         TEXT
```

## Assuntos do bloco

Cada linha representa um único assunto dentro de um bloco. O bloco pode conter diversas matérias porque suas linhas podem apontar para diferentes registros de `planejamento_materias`.

```text
planejamento_bloco_assuntos
---------------------------
id                         BIGSERIAL PRIMARY KEY
planejamento_bloco_fk      BIGINT NOT NULL
planejamento_materia_fk    BIGINT NOT NULL
assunto_fk                 BIGINT NOT NULL
ordem                      INTEGER
data_prevista              DATE
observacoes                TEXT

UNIQUE (planejamento_bloco_fk, assunto_fk)
```

O backend deverá validar que `assunto_fk` pertence à matéria indicada por `planejamento_materia_fk`.

## Etapas genéricas do assunto

As etapas não serão colunas fixas como `assunto_concluido`, `resumo_concluido` ou `questoes_concluidas`. Elas serão linhas relacionadas aos tipos de registro.

```text
planejamento_assunto_etapas
---------------------------
id                              BIGSERIAL PRIMARY KEY
planejamento_bloco_assunto_fk   BIGINT NOT NULL
tipo_registro_fk                BIGINT NOT NULL
concluida                       BOOLEAN NOT NULL DEFAULT FALSE
data_prevista                   DATE
data_concluida                  DATE
observacoes                     TEXT
ordem                           INTEGER

UNIQUE (planejamento_bloco_assunto_fk, tipo_registro_fk)
```

Exemplos de etapas:

```text
Assunto
Resumo
Questões
Revisão
Simulado
```

Novos tipos poderão ser usados no planejamento sem adicionar novas colunas ao banco.

## Tipos de registro

A tabela existente é a origem dos tipos das etapas:

```text
tipos_registro
--------------
id              BIGSERIAL PRIMARY KEY
nome            VARCHAR(50) NOT NULL UNIQUE
```

Como a associação será feita por `tipo_registro_fk`, o backend não precisará comparar textos como `Assunto` ou `Resumo` para localizar uma etapa.

## Relação com os registros

Cada registro representa uma atividade efetivamente realizada. Um assunto ou uma etapa pode possuir vários registros.

Campos propostos para `registros`:

```text
concluido                  BOOLEAN NOT NULL DEFAULT FALSE
planejamento_etapa_fk      BIGINT NULL
```

O restante dos dados realizados continua em `registros`, incluindo:

```text
questoes_feitas
questoes_acertadas
data_estudo
tempo_estudado
observacoes
```

Exemplo:

```text
Etapa: Questões
├── Registro 1: 20 feitas e 15 acertadas
├── Registro 2: 30 feitas e 25 acertadas
└── Registro 3: 50 feitas, 42 acertadas e concluido = true
```

Os totais serão calculados sobre os registros:

```text
questões feitas = SUM(questoes_feitas)
questões acertadas = SUM(questoes_acertadas)
aproveitamento = acertadas / feitas
```

Não será criada uma tabela específica para resultados de questões. Uma tabela separada só seria necessária se existissem metas previamente cadastradas, como `50 questões da FGV` e `100 questões do Cebraspe`.

## Regra de conclusão

O formulário de registro terá um checkbox fixo chamado `concluido`.

Quando um registro relacionado a uma etapa for salvo como concluído, o backend atualizará a etapa:

```text
registro.concluido = true
→ etapa.concluida = true
→ etapa.data_concluida = registro.data_estudo
```

Ao desmarcar ou excluir um registro, o backend deverá verificar se ainda existe outro registro concluído para a mesma etapa. A etapa só voltará a ficar pendente quando não houver nenhum.

## Situação atual do backend

Já foram removidos do modelo `Registro`:

- `revisao_fk_assunto`;
- `revisao_complemento`;
- `questoes_revisao_feitas`;
- `questoes_revisao_acertadas`.

Ainda não foram implementados:

- as tabelas do planejamento descritas neste documento;
- o campo `registros.concluido`;
- o relacionamento `registros.planejamento_etapa_fk`;
- as entidades, serviços e endpoints do novo planejamento;
- a atualização automática das etapas.

## Ajuste necessário no modelo atual

Atualmente, a propriedade Java `Registro.planejamento` está mapeada para a coluna `ciclo_fk` e usa o tipo `Ciclo`. Na implementação futura, os conceitos deverão ser separados:

```text
Registro.ciclo         → ciclo_fk
Registro.planejamento  → planejamento_fk, caso ainda seja necessário
Registro.etapa         → planejamento_etapa_fk
```

Também deverá ser decidido se `planejamento_fk` continuará em `registros`. Ele pode ser derivado por meio da etapa, do assunto do bloco e do planejamento, portanto mantê-lo poderá gerar redundância.

## Pontos ainda em aberto

- Se `planejamento_materias` será mantida ou se as matérias serão sempre derivadas dos assuntos.
- Se todo assunto receberá automaticamente todas as etapas ou se o usuário escolherá quais etapas deseja.
- Se a conclusão será armazenada em `planejamento_assunto_etapas` ou calculada diretamente pela existência de registros concluídos.
- Como será determinada a conclusão automática de um bloco e de uma matéria.
- Se um mesmo assunto poderá aparecer em mais de um bloco do mesmo planejamento.
