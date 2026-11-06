# Ideia: ciclos de estudo

> Nota para discussão. Esta proposta ainda não foi implementada.

## Objetivo

Permitir que o usuário organize matérias em ciclos de alternância. Cada ciclo tem
uma sequência própria de matérias e pode representar uma frente de estudo.

Exemplo:

```text
Ciclo PT / Matemática
├── Português
└── Matemática

Ciclo Administrativo / Constitucional
├── Direito Administrativo
└── Direito Constitucional

Ciclo TI
├── AFO
├── Banco de Dados
├── Infraestrutura
└── Desenvolvimento
```

O usuário poderá montar quantos ciclos quiser. Nesta primeira versão, o objetivo
é somente cadastrar e ordenar as matérias de cada ciclo.

## Estrutura proposta

```text
ciclos
└── ciclo_materias
```

`nome` é uma coluna de `ciclos`; não é necessária uma tabela chamada
`ciclo_nome`.

## Ciclos

```text
ciclos
------
id              BIGSERIAL PRIMARY KEY
usuario_fk      BIGINT NOT NULL
nome            VARCHAR(150) NOT NULL
ativo           BOOLEAN NOT NULL DEFAULT TRUE
observacoes     TEXT
criado_em       TIMESTAMP NOT NULL
atualizado_em   TIMESTAMP NOT NULL
```

Cada ciclo pertence a um usuário e possui um nome que explique a combinação de
matérias, como `PT / Matemática` ou `TI`.

## Matérias do ciclo

```text
ciclo_materias
--------------
id              BIGSERIAL PRIMARY KEY
ciclo_fk        BIGINT NOT NULL
materia_fk      BIGINT NOT NULL
ordem           INTEGER NOT NULL
ativo           BOOLEAN NOT NULL DEFAULT TRUE

UNIQUE (ciclo_fk, materia_fk)
UNIQUE (ciclo_fk, ordem)
```

Cada linha representa uma matéria na sequência do ciclo. A coluna `ordem`
define a alternância: ao concluir uma matéria, a próxima será a que tiver a
ordem seguinte.

As restrições garantem que uma matéria não seja incluída duas vezes no mesmo
ciclo e que duas matérias não ocupem a mesma posição.

## Fora do escopo inicial

Os ciclos não obrigam o usuário a estudar todos eles em um dia. Eles serão
independentes: caso o usuário não estude um ciclo hoje, ele poderá retomá-lo no
dia seguinte de onde parou.

Por enquanto, não serão implementados:

- metas diárias ou quantidade de matérias por ciclo;
- horários;
- indicação da matéria atual/próxima;
- avanço automático ao registrar estudo;
- histórico das passagens pelo ciclo.

Quando o uso do ciclo for implementado, será necessário definir onde guardar a
posição atual — possivelmente em uma coluna ou tabela de progresso — e qual ação
faz o ciclo avançar. Essa definição fica separada do cadastro para não antecipar
regras que ainda podem mudar.
