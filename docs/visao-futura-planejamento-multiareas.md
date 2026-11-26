# Visão futura: planejamento em múltiplas áreas

> Documento de discussão criado em 17 de novembro de 2026.
> Nenhuma mudança descrita aqui está aprovada para implementação.
> O foco atual da aplicação continua sendo o planejamento para concursos.

## Motivação

A aplicação poderá, no futuro, unificar o acompanhamento das principais frentes de estudo e trabalho:

```text
CONCURSO
TECNOLOGIA
TRABALHO
BASE/PESQUISA
```

A intenção não é criar quatro sistemas independentes. O objetivo é manter um núcleo comum para planejamentos, execução e tempo registrado, com formas de organização adequadas a cada situação.

## Conceitos separados

Três conceitos não devem ser confundidos:

```text
Categoria     Em qual área o planejamento se encontra
Planejamento  Qual objetivo se pretende alcançar
Modelo        Como o conteúdo ou trabalho será organizado
```

Exemplos:

```text
Categoria: CONCURSO
Planejamento: TRT 2027
Modelo: CONTEUDO

Categoria: TECNOLOGIA
Planejamento: Construir uma API financeira
Modelo: ATIVIDADES

Categoria: TECNOLOGIA
Planejamento: Formação em Tecnologia
Modelo: CONTEUDO

Categoria: BASE/PESQUISA
Planejamento: Fundamentos de Ciência da Computação
Modelo: CONTEUDO

Categoria: BASE/PESQUISA
Planejamento: Pesquisa sobre sistemas de recomendação
Modelo: ATIVIDADES
```

A categoria, portanto, não determina sozinha a estrutura do planejamento.

## Categorias

A entidade `Categoria` já existe e atualmente é associada às matérias por uma relação muitos-para-muitos. A ideia futura é tratá-la como uma taxonomia transversal da aplicação.

Entidades independentes que poderão receber categorias próprias:

- `Materia` (relação já existente);
- `Planejamento`;
- `MaterialGuia`;
- possivelmente `Ciclo`, caso exista necessidade de filtrá-lo por área.

Possíveis tabelas de relacionamento:

```text
materia_categorias             existente
planejamento_categorias        futuro
material_guia_categorias       futuro e opcional
ciclo_categorias               futuro e opcional
```

Entidades internas, como matérias, grupos, assuntos e etapas de um planejamento, não precisam repetir a mesma classificação. Elas podem herdar as categorias do planejamento ao qual pertencem.

Uma mesma entidade poderá aparecer em mais de uma categoria. Por exemplo, `Algoritmos` pode pertencer a `TECNOLOGIA` e `BASE/PESQUISA`.

## Modelos de planejamento

Uma possibilidade é adicionar ao planejamento um modelo de organização:

```text
CONTEUDO
ATIVIDADES
```

Os nomes ainda são provisórios.

### Modelo CONTEUDO

Corresponde ao planejamento estruturado que já existe para concursos:

```text
Planejamento
└── Matérias ou grupos de matérias
    └── Grupos de assuntos
        └── Assuntos
            └── Etapas baseadas em tipos de registro
```

Exemplos de etapas: teoria, resumo, revisão, questões e simulado.

Esse modelo atende principalmente concursos, cursos estruturados, certificações e formação sistemática de base.

O modelo também pode atender ao estudo de tecnologia sem exigir que todo o
conteúdo seja catalogado antecipadamente. Nesse caso, os assuntos representam
divisões amplas e estáveis da matéria, enquanto o conteúdo específico estudado
em uma sessão fica apenas no registro.

O fluxo atual de concurso deve ser preservado. A ampliação para outras áreas não deve exigir assuntos artificiais nem enfraquecer suas validações.

### Modelo ATIVIDADES

Atende trabalhos em que os itens não partem necessariamente de matérias e assuntos previamente cadastrados:

```text
Planejamento
└── Atividades
    └── Subatividades
```

Exemplos de uso:

- desenvolvimento de um projeto de tecnologia;
- projeto de pesquisa;
- entregas e metas de trabalho;
- atividades livres que surjam durante a execução.

Uma estrutura possível é uma tabela hierárquica:

```text
planejamento_atividades
-----------------------
id                         BIGSERIAL PRIMARY KEY
planejamento_fk            BIGINT NOT NULL
atividade_pai_fk           BIGINT NULL
titulo                     VARCHAR(200) NOT NULL
descricao                  TEXT
ordem                      INTEGER
status                     VARCHAR(30)
prioridade                 SMALLINT
data_prevista              DATE
data_concluida             DATE
materia_fk                 BIGINT NULL
assunto_fk                 BIGINT NULL
observacoes                TEXT
```

`atividade_pai_fk` permite usar a mesma estrutura para fases e tarefas:

```text
Leitura de artigos
├── Buscar artigos
├── Selecionar fontes
└── Produzir resumos
```

Possíveis estados:

```text
PENDENTE
EM_ANDAMENTO
BLOQUEADA
CONCLUIDA
CANCELADA
```

## Por que a etapa atual do assunto não resolve todos os casos

`PlanejamentoItemEtapa` está ligado obrigatoriamente a um item de assunto e a um tipo de registro. Ele representa corretamente etapas como teoria, revisão e questões de um assunto conhecido.

Uma atividade de pesquisa ou trabalho possui outra semântica:

- tem título próprio;
- pode existir sem matéria e assunto;
- pode conter subtarefas;
- pode representar uma fase, entrega ou marco;
- não é necessariamente uma etapa baseada em `TipoRegistro`.

Generalizar a tabela atual exigiria tornar opcionais várias relações centrais e misturaria dois modelos diferentes. A opção mais segura, caso essa ideia seja implementada, é preservar `PlanejamentoItemEtapa` e criar uma estrutura de atividades separada.

## Materiais-guia

`MaterialGuia` representa uma fonte identificável que orienta o trabalho ou estudo, por exemplo:

```text
Effective Java
Formação Java Developer - DIO
Curso de Spring Boot - Udemy
Documentação oficial do Java
Playlist de Estrutura de Dados
```

Um material-guia pode ser livro, curso, formação, trilha, playlist, documentação, artigo ou vídeo isolado quando acompanhado individualmente.

O cadastro deve ficar em um nível intermediário. `Livro` ou `YouTube` são genéricos demais; uma página ou um pequeno trecho de aula normalmente são específicos demais.

Evoluções que poderão ser avaliadas:

```text
MaterialGuia
├── tipo: MaterialTipo
├── link
├── observações
└── matérias relacionadas
```

A relação entre materiais-guia e matérias tende a ser muitos-para-muitos, pois uma formação pode abordar Java, Spring Boot, banco de dados e APIs.

As categorias de um material poderão inicialmente ser derivadas das matérias relacionadas. Uma associação direta com categorias só será necessária se a classificação do material precisar ser independente delas.

## Refinamento proposto para Tecnologia

O planejamento de estudo em Tecnologia deve evitar o cadastro detalhado de
cada capítulo, aula ou conceito encontrado. A estrutura inicial pode continuar
próxima ao planejamento de conteúdo atual, acrescida de materiais e projetos de
aplicação:

```text
Planejamento
└── Matérias
    ├── Assuntos amplos
    ├── Materiais
    └── Projetos
```

Os conceitos possuem responsabilidades diferentes:

```text
Matéria       Domínio de conhecimento acompanhado no planejamento
Assunto       Divisão ampla e reutilizável da matéria
Material      Fonte usada para adquirir ou revisar conhecimento
Projeto       Local em que o conhecimento pode ser aplicado
Registro      Evidência da atividade realmente executada
```

### Exemplos de organização

```text
Planejamento: Formação em Tecnologia

Matéria: Desenvolvimento Web

Assuntos
├── HTML
└── Java

Materiais
├── W3Schools - HTML
└── W3Schools - Java

Projetos
└── Aplicar os conhecimentos em um sistema existente
```

```text
Matéria: Engenharia de Software

Assuntos
├── Arquitetura
└── SOLID

Materiais
├── Livro de Engenharia de Software - Marco Túlio
└── Livro Princípios de SOLID

Projetos
└── Aplicar princípios no projeto de Precatórios
```

Um material ou projeto pode se relacionar a um ou vários assuntos. Essa
associação deve ser opcional para que o planejamento não gere trabalho de
classificação desnecessário. Quando informada, ela ajuda a sugerir os itens
relevantes durante a criação de um registro.

### Granularidade dos assuntos

Os assuntos já existentes continuam sendo utilizados, mas devem representar
divisões suficientemente amplas. Um assunto deve agrupar muitas sessões,
diferentes materiais e possivelmente mais de um projeto.

Detalhes menores não precisam ser cadastrados como assuntos permanentes. Eles
podem ser informados em um campo livre do registro:

```text
Matéria: Engenharia de Software
Assunto: SOLID
Conteúdo realizado: Princípio da responsabilidade única
```

```text
Matéria: Desenvolvimento Web
Assunto: HTML
Conteúdo realizado: Validação de formulários
```

Com isso, conceitos como princípio da responsabilidade única, formulários,
tags semânticas ou um capítulo específico permanecem no histórico, sem exigir
novos cadastros.

### Etapas do planejamento de Tecnologia

Uma configuração inicial de etapas pode ser:

```text
ASSUNTO
QUESTOES_PERGUNTAS
RESUMO
REVISAO
PRATICA
```

As etapas organizam os registros, mas não precisam impor uma sequência rígida.
O usuário pode realizar várias sessões da mesma etapa, voltar a uma etapa
anterior ou iniciar diretamente pela prática.

O termo `ASSUNTO` possui dois usos que devem ser diferenciados na interface:

```text
Assunto selecionado: SOLID
Etapa do registro: ASSUNTO (estudo do conteúdo)
```

A nomenclatura existente pode ser preservada, com um texto auxiliar que
explique o significado da etapa.

### Registro simples de Tecnologia

O formulário comum pode conter somente:

```text
matéria                     obrigatória
assunto                     obrigatório
etapa                       obrigatória
material                    opcional
projeto                     opcional
conteúdo realizado          texto curto e opcional
tempo                       obrigatório
observações                 opcionais
```

Os campos podem ganhar destaque conforme a etapa. Em
`QUESTOES_PERGUNTAS`, podem aparecer quantidade, acertos, pergunta e resposta.
Em `PRATICA`, o projeto recebe destaque. Material e projeto não são
mutuamente exclusivos: o primeiro identifica a fonte do conhecimento e o
segundo identifica onde ele foi aplicado.

Exemplo de estudo:

```text
Matéria: Engenharia de Software
Assunto: SOLID
Etapa: ASSUNTO
Material: Livro Princípios de SOLID
Conteúdo realizado: Princípio da responsabilidade única
Tempo: 00:40
```

Exemplo de aplicação:

```text
Matéria: Engenharia de Software
Assunto: SOLID
Etapa: PRATICA
Material: Livro Princípios de SOLID
Projeto: Sistema de Precatórios
Conteúdo realizado: Separar responsabilidades do serviço de cálculo
Tempo: 01:20
```

Quando o registro for iniciado a partir de um material, projeto ou item do
planejamento, os dados conhecidos devem vir previamente preenchidos. A
interface deve pedir apenas o que ainda for necessário para descrever a sessão.

## Pesquisa

Pesquisa não deve ser cadastrada como uma matéria genérica. Ela representa o tipo ou o contexto da atividade. Os temas podem surgir durante a execução e não devem obrigatoriamente poluir o catálogo permanente de assuntos.

Exemplo de planejamento:

```text
Pesquisa sobre sistemas de recomendação
├── Introdução
│   ├── Delimitar o tema
│   └── Definir os objetivos
├── Formar base em algoritmos
├── Leitura de artigos
├── Desenvolvimento ou experimento
└── Conclusão
```

No registro de uma sessão de pesquisa:

```text
tipo de registro: Pesquisa
planejamento: Pesquisa sobre sistemas de recomendação
atividade: Leitura de artigos
título: Comparação de algoritmos de recomendação
matéria: Algoritmos (opcional)
assunto: Sistemas de recomendação (opcional)
material-guia: artigo lido (opcional)
tempo estudado: 01:20
observações: resumo, fontes e conclusões
```

Isso poderá exigir, no futuro:

- um campo livre `titulo` em `Registro`;
- relacionamento opcional entre `Registro` e `PlanejamentoAtividade`;
- revisão da obrigatoriedade de matéria e assunto no registro;
- validação condicional conforme o modelo ou tipo do registro.

## Trabalho

O modelo de atividades também pode organizar demandas de trabalho:

```text
Entrega do módulo financeiro
├── Levantar requisitos
├── Definir arquitetura
├── Implementar endpoint
├── Criar testes
└── Publicar versão
```

O planejamento pode representar o objetivo ou a entrega, evitando criar desde o início um sistema genérico e complexo de metas. Metas quantitativas poderão ser avaliadas posteriormente, quando existirem casos concretos como:

- estudar dez horas por semana;
- ler vinte artigos;
- entregar três funcionalidades;
- concluir determinado percentual até uma data.

## Registros como ponto comum

Independentemente da categoria ou do modelo, `Registro` pode continuar sendo o histórico da execução real:

```text
o que foi feito
quando foi feito
quanto tempo foi gasto
em qual planejamento
em qual atividade, matéria ou assunto
qual material foi utilizado
o que foi concluído ou descoberto
```

Para planejamentos de conteúdo, o registro continua apontando para a etapa do assunto. Para planejamentos de atividades, ele poderá apontar para uma atividade. Essas relações devem ser validadas para impedir combinações incompatíveis.

Registros avulsos poderão derivar suas categorias da matéria ou do material-guia. Caso seja necessário registrar exatamente a finalidade de uma sessão avulsa, poderá ser avaliada uma categoria opcional diretamente no registro.

## Visualização futura

Uma navegação possível:

```text
[ Concurso ] [ Tecnologia ] [ Trabalho ] [ Base/Pesquisa ]
```

Cada aba filtra os mesmos conceitos principais, mas pode destacar informações diferentes:

| Área | Destaques |
| --- | --- |
| Concurso | matérias, pesos, ciclos, questões, revisões e prazo da prova |
| Tecnologia | cursos, materiais-guia, projetos práticos e progresso |
| Trabalho | atividades, prioridade, bloqueios, prazos e entregas |
| Base/Pesquisa | fundamentos, fontes, anotações, etapas e conclusões |

## Sequência sugerida, caso a ideia avance

1. Manter o desenvolvimento e a estabilização do fluxo de concurso.
2. Adicionar categorias aos planejamentos.
3. Definir formalmente os modelos de planejamento.
4. Criar `PlanejamentoAtividade` sem alterar as etapas atuais dos assuntos.
5. Permitir que registros apontem para atividades.
6. Avaliar título livre e matéria/assunto opcionais nos registros.
7. Evoluir o cadastro e a relação dos materiais-guia.
8. Criar a navegação pelas quatro categorias.
9. Somente depois avaliar metas quantitativas e indicadores gerais.

## Decisões em aberto

- Nomes definitivos dos modelos `CONTEUDO` e `ATIVIDADES`.
- Se um planejamento poderá ter um ou vários modelos simultaneamente.
- Se categorias de materiais-guia serão próprias ou derivadas das matérias.
- Se ciclos precisarão de categorias próprias.
- Quando matéria e assunto poderão ser opcionais em um registro.
- Como distinguir fase, tarefa, marco e entrega dentro das atividades.
- Quais metas quantitativas realmente serão necessárias.
- Se dados de trabalho exigirão isolamento ou regras de privacidade adicionais.

## Fora do escopo atual

Este documento não autoriza mudanças no banco, nas entidades ou nas APIs. Ele preserva a ideia para avaliação futura, enquanto a prioridade permanece no módulo de concursos.

