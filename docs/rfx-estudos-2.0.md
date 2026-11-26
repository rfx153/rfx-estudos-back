# RFX Estudos 2.0 — Contextualização, requisitos e planejamento de evolução

## 1. Contextualização do projeto

O RFX Estudos é uma aplicação destinada ao registro, planejamento, gerenciamento e acompanhamento de atividades de estudo.

Atualmente, o sistema possui uma estrutura predominantemente orientada à preparação para concursos públicos, permitindo registrar sessões de estudo, organizar matérias e assuntos, acompanhar planejamentos, estabelecer metas e gerenciar ciclos de estudo.

A aplicação encontra-se em funcionamento e possui uma versão estável em produção.

### 1.1. Stack tecnológica

- Frontend: Angular.
- Backend: Java com Spring Boot.
- Banco de dados: PostgreSQL.
- Hospedagem do frontend: Vercel.
- Hospedagem do backend: Render.
- Banco de dados de produção: Neon.
- Versionamento: Git.

### 1.2. Módulos existentes

- Início / Dashboard.
- Novo Registro.
- Histórico.
- Planejamento.
- Metas.
- Ciclos.
- Caminho.
- Acompanhamento.
- Metodologia.
- Gerenciador de Dados.

As funcionalidades existentes deverão ser preservadas durante o processo de evolução da aplicação.

## 2. Objetivo da evolução

O objetivo do RFX Estudos 2.0 é ampliar a finalidade da aplicação, permitindo o gerenciamento de diferentes modalidades de atividades intelectuais, acadêmicas, tecnológicas e profissionais.

O sistema deverá deixar de ser exclusivamente orientado aos estudos para concursos públicos e passar a contemplar:

- Estudos para concursos públicos.
- Aprendizado e desenvolvimento tecnológico.
- Estudos de fundamentos da computação e matemática.
- Pesquisa científica.
- Atividades profissionais.
- Desenvolvimento e acompanhamento de projetos.

A evolução deverá ocorrer por meio do reaproveitamento das estruturas existentes, evitando uma reconstrução completa da aplicação.

## 3. Princípios arquiteturais

O desenvolvimento deverá considerar os seguintes princípios:

1. Preservar as funcionalidades existentes.
2. Manter a compatibilidade com os dados atuais.
3. Reaproveitar entidades, tabelas, serviços e componentes sempre que possível.
4. Evitar complexidade arquitetural desnecessária.
5. Centralizar os registros de atividades em uma única tabela.
6. Separar os contextos da aplicação por categorias.
7. Permitir diferentes modalidades de planejamento.
8. Manter o cadastro de materiais de aprendizado simples.
9. Desenvolver e testar as alterações em ambiente isolado.
10. Realizar alterações no banco de dados por meio de migrations versionadas.
11. Evitar alterações destrutivas sem análise prévia de impacto.

## 4. Perfis da aplicação

### 4.1. Conceito de perfil

A aplicação deverá possuir quatro perfis de utilização e uma visão geral.

O perfil representa o contexto em que o usuário está utilizando a aplicação.

A proposta é reaproveitar a tabela de categorias existente, sem criar uma entidade específica de perfil, desde que a análise do código confirme a viabilidade dessa abordagem.

| Categoria | Nome apresentado |
|---|---|
| CONCURSO | Concursos |
| TECNOLOGIA | Tecnologia |
| BASE | Base e Pesquisa |
| TRABALHO | Trabalho |

As categorias BASE, CONCURSO e TECNOLOGIA já existem no sistema. A categoria TRABALHO deverá ser adicionada.

### 4.2. Perfil Concursos

Deverá preservar as funcionalidades existentes relacionadas à preparação para concursos públicos.

Principais elementos:

- Matérias.
- Assuntos.
- Planejamentos de concursos.
- Blocos de matérias.
- Estudo de conteúdo.
- Resumos.
- Questões.
- Revisões.
- Materiais-guia.
- Metas de estudo.
- Ciclos de estudo.
- Indicadores de desempenho.

O comportamento atual deverá ser mantido sempre que possível.

### 4.3. Perfil Tecnologia

Destinado ao aprendizado tecnológico e ao desenvolvimento de competências técnicas.

Exemplos:

- Desenvolvimento de software.
- Linguagens de programação.
- Engenharia de software.
- Inteligência artificial.
- Arquitetura de software.
- Banco de dados.
- Infraestrutura.
- Sistemas distribuídos.

Deverá permitir:

- Organização por matérias e assuntos.
- Cadastro de materiais-guia.
- Planejamentos de aprendizado.
- Registro de atividades práticas.
- Desenvolvimento de projetos.
- Associação de projetos finais aos planejamentos.
- Acompanhamento do tempo dedicado às atividades.

Um planejamento tecnológico poderá combinar estudo teórico e desenvolvimento prático.

### 4.4. Perfil Base e Pesquisa

Destinado ao aprofundamento acadêmico e científico.

Deverá contemplar duas modalidades complementares:

**Estudo de fundamentos:**

- Matemática.
- Algoritmos.
- Estruturas de dados.
- Teoria da computação.
- Fundamentos da computação.
- Probabilidade e estatística.
- Demais disciplinas acadêmicas.

**Pesquisa científica:**

- Leitura de artigos.
- Revisão bibliográfica.
- Definição de problemas de pesquisa.
- Elaboração de metodologias.
- Desenvolvimento de experimentos.
- Escrita de artigos científicos.
- Projetos de pesquisa.

O perfil BASE deverá permitir tanto atividades organizadas por matérias e assuntos quanto atividades organizadas por projetos e tarefas.

### 4.5. Perfil Trabalho

Destinado ao acompanhamento de atividades profissionais.

Diferentemente do perfil Concursos, não deverá exigir a existência de matérias e assuntos.

A organização deverá ocorrer predominantemente por:

- Projetos.
- Etapas.
- Tarefas.
- Atividades profissionais.
- Registros de tempo.
- Observações.

Exemplos de atividades:

- Desenvolvimento de funcionalidades.
- Modelagem de banco de dados.
- Análise de requisitos.
- Reuniões.
- Testes.
- Documentação.
- Manutenção de sistemas.

O objetivo não é transformar o RFX em uma ferramenta completa de gerenciamento corporativo de projetos, mas permitir o planejamento e acompanhamento pessoal das atividades profissionais.

### 4.6. Visão Geral

A visão Geral deverá apresentar informações consolidadas dos quatro perfis.

Não deverá representar uma categoria persistida no banco de dados.

Deverá permitir:

- Visualizar todos os registros.
- Visualizar todos os planejamentos.
- Consultar indicadores consolidados.
- Consultar metas gerais e específicas.
- Filtrar informações por categoria.
- Acompanhar a distribuição do tempo entre as diferentes atividades.

Ao criar um registro pela visão Geral, o usuário deverá selecionar a categoria correspondente.

### 4.7. Comportamento da interface

A aplicação deverá possuir um seletor de perfil.

A seleção deverá influenciar:

- Dados apresentados.
- Formulários.
- Planejamentos disponíveis.
- Tipos de registro disponíveis.
- Indicadores.
- Metas.
- Funcionalidades aplicáveis ao contexto.

Deverá ser avaliada a persistência da última categoria selecionada durante a navegação.

## 5. Estrutura conceitual da aplicação

A evolução do sistema deverá considerar os seguintes conceitos:

| Conceito | Responsabilidade |
|---|---|
| Categoria | Contexto da atividade |
| Planejamento | Organização das atividades pretendidas |
| Matéria | Área de conhecimento |
| Assunto | Conteúdo específico de uma matéria |
| Projeto | Iniciativa com objetivo definido |
| Etapa de projeto | Fase de execução |
| Tarefa | Atividade planejada de um projeto |
| Material-guia | Recurso de aprendizado ou referência |
| Tipo de registro | Natureza da atividade realizada |
| Registro | Atividade efetivamente realizada |
| Meta | Resultado ou esforço esperado |

A aplicação deverá possuir dois eixos complementares de organização:

### Conhecimento

- Matérias.
- Assuntos.
- Materiais-guia.
- Estudos.
- Revisões.
- Questões.

### Execução

- Projetos.
- Etapas.
- Tarefas.
- Atividades.
- Resultados.

Concursos utilizará predominantemente o eixo de conhecimento.

Trabalho utilizará predominantemente o eixo de execução.

Tecnologia e Base/Pesquisa poderão utilizar ambos.

## 6. Refatoração do módulo de registros

### 6.1. Diretriz principal

A tabela `registros` deverá continuar sendo uma tabela única para armazenamento das atividades realizadas.

Não se pretende criar tabelas independentes de registros para cada perfil.

Os campos aplicáveis deverão ser determinados pela categoria e pelo tipo de registro.

Campos não aplicáveis poderão permanecer nulos.

### 6.2. Estrutura atual conhecida

A tabela `public.registros` possui atualmente as seguintes colunas:

| Coluna | Tipo atual |
|---|---|
| id | serial4 |
| planejamento_fk | int4 |
| materia_fk | int4 |
| assunto_fk | int4 |
| material_tipo_fk | int4 |
| material_nome | varchar(255) |
| ponto_parada | varchar(255) |
| questoes_feitas | int4 |
| questoes_acertadas | int4 |
| revisao_fk_assunto | int4 |
| revisao_complemento | text |
| data_estudo | date |
| tempo_estudado | time |
| link_documento | text |
| observacoes | text |
| questoes_revisao_feitas | int4 |
| questoes_revisao_acertadas | int4 |
| tipo_registro_fk | int8 |
| material_guia_fk | int8 |
| concluido | bool |
| planejamento_etapa_fk | int8 |
| horario_inicio | time |

Atualmente, matéria e assunto são obrigatórios no banco de dados.

A tabela já possui relacionamentos com:

- Planejamentos.
- Etapas de planejamento.
- Matérias.
- Assuntos.
- Tipos de material.
- Materiais-guia.
- Tipos de registro.

Também possui índices relacionados à data de estudo, matéria, planejamento, etapa, material-guia e tipo de registro.

### 6.3. Alterações propostas

Avaliar as seguintes alterações:

- Adicionar `categoria_fk`.
- Tornar `materia_fk` opcional no banco de dados.
- Tornar `assunto_fk` opcional no banco de dados.
- Adicionar referência a projetos.
- Adicionar referência a etapas de projetos.
- Adicionar referência a tarefas de projetos.
- Preservar os campos existentes.
- Criar índices adequados às consultas por categoria.
- Implementar validações condicionais.

A categoria deverá ser identificada diretamente no registro, pois atividades profissionais poderão não possuir matéria associada.

Os tipos das novas chaves estrangeiras deverão ser compatíveis com as chaves primárias das tabelas referenciadas.

A necessidade de armazenar simultaneamente projeto, etapa e tarefa deverá ser avaliada, considerando possíveis redundâncias e inconsistências.

### 6.4. Tipos de registro

A tabela `tipos_registro` deverá ser reaproveitada e ampliada.

Deverão ser analisados os tipos existentes antes de adicionar novos valores.

Possíveis modalidades:

- Estudo de conteúdo.
- Resumo.
- Questões.
- Revisão.
- Leitura.
- Exercício prático.
- Desenvolvimento.
- Pesquisa científica.
- Reunião.
- Documentação.
- Testes.
- Análise.
- Manutenção.

Os tipos poderão ser compartilhados entre categorias.

Deverá ser avaliada a necessidade de uma associação entre categorias e tipos de registro para determinar quais modalidades estão disponíveis em cada perfil.

### 6.5. Validações

As regras de obrigatoriedade deverão considerar o contexto da atividade.

Exemplos:

- Registros de concursos deverão preservar as validações existentes.
- Registros de trabalho não deverão exigir matéria e assunto.
- Registros de desenvolvimento poderão estar vinculados a projetos e tarefas.
- Registros de aprendizado poderão estar vinculados a materiais-guia.
- Registros de pesquisa poderão estar vinculados a projetos científicos.
- Registros de questões deverão validar a quantidade de questões e acertos.
- O planejamento, projeto, tarefa e categoria deverão possuir relacionamentos consistentes.

A flexibilização das constraints de matéria e assunto não deverá permitir registros inválidos.

### 6.6. Formulários

O formulário de Novo Registro deverá ser adaptado conforme a categoria e o tipo selecionados.

Deverão ser reaproveitados componentes compartilhados, evitando duplicação desnecessária.

Campos comuns:

- Categoria.
- Tipo de registro.
- Data.
- Horário de início.
- Duração.
- Planejamento, quando aplicável.
- Observações.
- Conclusão, conforme sua semântica.

Campos específicos poderão incluir:

- Matéria.
- Assunto.
- Material-guia.
- Material utilizado.
- Ponto de parada.
- Questões.
- Acertos.
- Projeto.
- Etapa.
- Tarefa.

### 6.7. Exemplo de registros

| Campo | Concurso | Tecnologia | Base | Trabalho |
|---|---|---|---|---|
| Categoria | CONCURSO | TECNOLOGIA | BASE | TRABALHO |
| Tipo | Questões | Estudo | Pesquisa | Desenvolvimento |
| Matéria | Português | Java | Não aplicável | Não aplicável |
| Assunto | Sintaxe | Collections | Não aplicável | Não aplicável |
| Material-guia | Estratégia | Alura | Artigo científico | Não aplicável |
| Projeto | Não aplicável | Opcional | Artigo científico | Sistema MNI |
| Tarefa | Não aplicável | Opcional | Revisão bibliográfica | Integração SOAP |
| Duração | 60 min | 45 min | 60 min | 120 min |

Todos os exemplos deverão ser armazenáveis na mesma tabela.

### 6.8. Duração dos registros

Atualmente, o campo `tempo_estudado` utiliza o tipo PostgreSQL `time`.

Esse tipo representa um horário, e não uma duração propriamente dita.

Deverá ser avaliada a possibilidade de migrar para uma representação mais adequada, como duração em segundos.

Entretanto, essa alteração não deverá ser realizada sem analisar seu impacto sobre:

- Consultas SQL.
- Entidades Java.
- DTOs.
- Metas.
- Indicadores.
- Gráficos.
- Componentes Angular.
- Dados existentes.

A alteração poderá ser postergada para uma etapa posterior.

## 7. Evolução dos materiais-guia

### 7.1. Objetivo

Ampliar o conceito de Material-guia sem criar um módulo complexo de biblioteca.

Atualmente, o Material-guia é utilizado principalmente para identificar cursos de concursos, como os cursos do Estratégia.

O cadastro deverá passar a contemplar também:

- Livros.
- Cursos.
- Vídeos.
- Sites.
- Documentações.
- Artigos científicos.
- Apostilas.
- Outros recursos.

### 7.2. Estrutura atual

A tabela `public.materiais_guia` possui:

| Coluna | Tipo |
|---|---|
| id | int4 |
| nome | varchar(150) |
| ativo | bool |

O nome possui restrição de unicidade.

Existe um índice sobre os campos ativo e nome.

### 7.3. Alterações propostas

Avaliar a inclusão de:

- `material_tipo_fk`.
- `descricao`.
- `link`.

A tabela `material_tipos` deverá ser analisada para verificar se pode ser reutilizada na classificação dos materiais-guia.

Os novos campos deverão inicialmente ser opcionais, preservando os materiais existentes.

Não deverá ser necessário cadastrar individualmente:

- Capítulos.
- Páginas.
- Aulas.
- PDFs internos de cursos.
- Exercícios.
- Autores.
- Editoras.
- ISBN.

O cadastro deverá permanecer simples.

### 7.4. Material-guia e material utilizado

O sistema já possui os seguintes campos na tabela de registros:

- `material_guia_fk`.
- `material_tipo_fk`.
- `material_nome`.
- `ponto_parada`.
- `link_documento`.

Esses campos deverão ser analisados e reaproveitados.

O Material-guia representa o recurso principal.

O material utilizado representa o conteúdo específico acessado durante uma sessão.

Exemplo:

- Material-guia: Formação Java — Alura.
- Material utilizado: Collections.
- Tipo do material utilizado: Vídeo.
- Ponto de parada: Aula 12.

Outro exemplo:

- Material-guia: Algoritmos — Cormen.
- Material utilizado: Capítulo 3.
- Ponto de parada: Página 78.

Em concursos, deverá continuar sendo possível cadastrar apenas o nome do curso principal, sem registrar individualmente todos os seus materiais.

### 7.5. Associação com planejamentos

Avaliar a criação da tabela associativa:

`planejamento_materiais_guia`

Campos conceituais:

- id.
- planejamento_fk.
- material_guia_fk.

Deverá existir uma restrição de unicidade para a combinação planejamento e material-guia.

Essa associação permitirá apresentar uma bibliografia ou lista de referências dentro do planejamento.

Um mesmo material poderá ser utilizado em diferentes planejamentos e categorias.

Inicialmente, não se pretende exigir uma associação direta entre material-guia e categoria.

### 7.6. Inconsistência identificada

A definição atual de `materiais_guia.id` utiliza:

`nextval('ciclos_estudo_id_seq'::regclass)`

Deverá ser investigado se a tabela está compartilhando indevidamente a sequência da tabela de ciclos de estudo.

Caso seja necessário corrigir, a migration deverá considerar:

- Identificadores existentes.
- Dependências da sequência.
- Valor máximo dos identificadores.
- Ownership da sequência.
- Compatibilidade com os dados atuais.

## 8. Refatoração do planejamento

### 8.1. Estrutura atual

O planejamento atual é predominantemente orientado a concursos.

Sua estrutura conceitual é:

Planejamento → Blocos de matérias → Matérias → Assuntos → Etapas de estudo.

As tabelas conhecidas incluem:

- `planejamentos`.
- `planejamento_grupos_materias`.
- `planejamento_materias`.
- `planejamento_grupo_assunto_itens`.
- `planejamento_item_etapas`.

O código e as demais tabelas deverão ser analisados para confirmar a hierarquia completa.

### 8.2. Tabela planejamentos

A tabela `planejamentos` possui atualmente:

- id.
- nome.
- descricao.
- data_fim_prevista.

A proposta inicial é adicionar `categoria_fk`.

A tabela principal deverá continuar sendo utilizada como estrutura central de planejamento.

Inicialmente, não se pretende criar tipos rígidos de planejamento, pois um mesmo planejamento poderá conter matérias e projetos.

### 8.3. Planejamento de matérias

A estrutura existente deverá ser preservada.

A tabela `planejamento_materias` possui informações relacionadas a:

- Planejamento.
- Matéria.
- Peso.
- Ordem.
- Dificuldade.
- Datas previstas.
- Datas de conclusão.
- Observações.
- Grupo de matérias.

A tabela `planejamento_grupos_materias` representa os blocos ou agrupamentos de matérias.

Essas estruturas poderão continuar sendo utilizadas nos perfis Concurso, Tecnologia e Base.

### 8.4. Etapas de estudo

A tabela `planejamento_item_etapas` possui:

- id.
- planejamento_grupo_assunto_item_fk.
- tipo_registro_fk.
- concluida.
- concluida_manualmente.
- ordem.
- data_prevista.
- data_concluida.
- observacoes.

Existe uma restrição de unicidade entre o item de assunto e o tipo de registro.

A etapa também depende obrigatoriamente de um item de assunto.

Por esse motivo, a proposta inicial é não reutilizar diretamente essa tabela para tarefas de projetos.

As etapas de estudo e sua integração com os registros deverão ser preservadas.

### 8.5. Modalidades de planejamento

O sistema deverá permitir:

**Planejamento de estudos:**

- Blocos.
- Matérias.
- Assuntos.
- Etapas de estudo.
- Materiais-guia.

**Planejamento de projetos:**

- Projetos.
- Etapas.
- Tarefas.
- Prazos.
- Progresso.

**Planejamento híbrido:**

- Blocos de matérias.
- Assuntos.
- Materiais-guia.
- Projetos.
- Etapas e tarefas.

### 8.6. Exemplo de planejamento tecnológico

Planejamento: Desenvolvimento de Software.

Bloco 1 — Fundamentos:

- Algoritmos.
- Estruturas de dados.
- Complexidade computacional.

Bloco 2 — Desenvolvimento Web:

- Java.
- Spring Boot.
- Angular.
- Banco de dados.

Materiais-guia:

- Livro de Algoritmos.
- W3Schools.
- Formação Java.
- Documentação Spring Boot.

Projeto final: Sistema de gerenciamento financeiro.

Etapas e tarefas:

- Levantamento de requisitos.
- Modelagem do banco.
- Desenvolvimento do backend.
- Desenvolvimento do frontend.
- Testes.
- Publicação.

O projeto final deverá complementar os conteúdos teóricos.

### 8.7. Exemplo de planejamento profissional

Planejamento: Projetos profissionais.

Projeto: Sistema MNI.

Atividades:

- Definição da arquitetura.
- Integração com sistemas processuais.
- Desenvolvimento de funcionalidades.
- Testes.
- Documentação.

Nesse contexto, não deverá ser necessário cadastrar matérias e assuntos.

### 8.8. Exemplo de planejamento acadêmico

Planejamento: Fundamentos da Computação e Pesquisa.

Conteúdos:

- Álgebra Linear.
- Cálculo.
- Algoritmos.
- Grafos.
- Teoria da Computação.

Projeto científico:

- Revisão bibliográfica.
- Definição da metodologia.
- Desenvolvimento de experimentos.
- Escrita do artigo.
- Submissão.

## 9. Estrutura de projetos

### 9.1. Objetivo

Permitir o gerenciamento simplificado de projetos em diferentes categorias.

Projetos não deverão ser exclusivos do perfil Trabalho.

Poderão ser utilizados em:

- Trabalho.
- Tecnologia.
- Base e Pesquisa.

### 9.2. Modelagem preliminar

A proposta inicial considera as seguintes tabelas.

**projetos**

- id.
- categoria_fk.
- nome.
- descricao.
- data_inicio.
- data_fim_prevista.
- data_finalizado.
- status.

**projeto_etapas**

- id.
- projeto_fk.
- nome.
- descricao.
- ordem.
- data_prevista.
- status.

**projeto_tarefas**

- id.
- projeto_etapa_fk.
- nome.
- descricao.
- ordem.
- data_prevista.
- data_concluida.
- status.

**planejamento_projetos**

- id.
- planejamento_fk.
- projeto_fk.
- ordem.
- peso.

Essa estrutura representa uma proposta conceitual e deverá ser validada antes da implementação.

Deverá ser avaliado se a separação entre etapas e tarefas é realmente necessária para a primeira versão.

Também deverá ser avaliada a necessidade de uma associação intermediária entre projetos e planejamentos.

### 9.3. Independência dos projetos

A proposta é permitir que projetos possam existir independentemente de um planejamento.

Quando necessário, poderão ser associados a um planejamento de aprendizado, pesquisa ou trabalho.

### 9.4. Integração com registros

Um registro poderá estar vinculado a um projeto, etapa ou tarefa.

Uma tarefa poderá possuir diversos registros associados.

O tempo dedicado à tarefa deverá ser calculado a partir dos registros.

A conclusão de uma sessão de atividade não deverá necessariamente representar a conclusão da tarefa.

A semântica do campo `concluido` existente deverá ser analisada antes de implementar essa integração.

## 10. Progresso dos planejamentos

Os planejamentos de concursos deverão preservar os cálculos atuais.

Para os novos planejamentos, deverá ser avaliada a apresentação separada de:

- Progresso das matérias.
- Progresso dos assuntos.
- Progresso dos projetos.
- Progresso das tarefas.
- Tempo dedicado.

Nos planejamentos híbridos, poderá ser utilizado um cálculo ponderado entre conteúdos e projetos.

A fórmula definitiva deverá ser proposta após a análise das regras existentes.

Os materiais-guia deverão funcionar inicialmente como referências, sem necessidade de possuir percentual próprio de conclusão.

## 11. Ajustes nos demais módulos

### 11.1. Dashboard

Deverá apresentar indicadores gerais e específicos por categoria.

Possíveis indicadores:

- Tempo total de atividades.
- Tempo de estudo.
- Tempo de trabalho.
- Distribuição de tempo por categoria.
- Tempo por matéria.
- Tempo por projeto.
- Progresso dos planejamentos.

Horas de trabalho não deverão ser contabilizadas automaticamente como horas de estudo.

### 11.2. Histórico

Deverá permitir:

- Visualização geral dos registros.
- Filtragem por categoria.
- Filtragem por tipo de registro.
- Filtragem por matéria.
- Filtragem por projeto.
- Filtragem por planejamento.
- Filtragem por período.

### 11.3. Metas

As funcionalidades atuais deverão ser preservadas, incluindo metas diárias, semanais, mensais e por matéria, conforme efetivamente implementadas no sistema.

Deverá ser avaliada a inclusão de:

- Metas por categoria.
- Metas por projeto.
- Metas gerais de atividades.
- Metas específicas de estudo.
- Metas relacionadas à conclusão de tarefas.

Um registro poderá contribuir para diferentes níveis de agregação, sem duplicação indevida nos totais.

### 11.4. Ciclos

Os ciclos existentes deverão ser preservados.

Sua disponibilidade deverá considerar a categoria selecionada.

Não será obrigatório disponibilizar ciclos para atividades profissionais ou projetos.

### 11.5. Caminho, Acompanhamento e Metodologia

Esses módulos deverão ser analisados individualmente.

Deverá ser determinado:

- Quais funcionalidades são específicas de concursos.
- Quais funcionalidades podem ser generalizadas.
- Quais deverão ser ocultadas em determinados perfis.
- Quais precisarão de alterações estruturais.

### 11.6. Gerenciador de Dados

Deverá contemplar, conforme necessário:

- Categorias.
- Matérias.
- Assuntos.
- Tipos de registro.
- Tipos de material.
- Materiais-guia.
- Projetos.
- Etapas e tarefas.

## 12. Planejamento de implementação

O desenvolvimento deverá ser dividido em seis etapas principais.

### Etapa 1 — Criação do banco de dados de desenvolvimento no Docker

Objetivo: criar um ambiente de desenvolvimento independente da produção.

Atividades:

- Identificar a versão do PostgreSQL utilizada no Neon.
- Criar configuração Docker Compose.
- Configurar volume persistente.
- Configurar credenciais exclusivas de desenvolvimento.
- Exportar a estrutura atual do banco.
- Importar dados necessários para testes, preferencialmente fictícios ou sanitizados.
- Configurar o backend para utilizar o banco local.
- Validar o funcionamento da versão atual.
- Definir estratégia de migrations.

Critério de conclusão:

A aplicação atual deverá funcionar integralmente utilizando o banco local, sem acessar o banco de produção.

### Etapa 2 — Criação da branch de desenvolvimento

Objetivo: preservar a versão estável em produção.

Atividades:

- Analisar os repositórios existentes.
- Identificar as branches atuais.
- Criar uma tag da versão estável.
- Criar a branch `develop` nos repositórios aplicáveis.
- Verificar configurações de deploy do Render.
- Verificar configurações de deploy da Vercel.
- Isolar as variáveis de ambiente.
- Definir o fluxo de integração de funcionalidades.

Fluxo proposto:

- `master`: produção.
- `develop`: integração do RFX 2.0.
- `feature/perfis`: desenvolvimento dos perfis.
- `feature/registros`: desenvolvimento dos registros.
- `feature/planejamento`: desenvolvimento do planejamento.
- `feature/integracao`: ajustes gerais.

Critério de conclusão:

Alterações de desenvolvimento não deverão afetar a aplicação nem o banco de produção.

### Etapa 3 — Ajustes e criação de perfis

Objetivo: implementar os contextos de utilização da aplicação.

Atividades:

- Analisar a tabela de categorias.
- Analisar relacionamentos existentes.
- Adicionar a categoria TRABALHO.
- Definir a associação de categorias aos registros.
- Definir a associação de categorias aos planejamentos.
- Elaborar estratégia de migração dos dados existentes.
- Implementar filtros por categoria no backend.
- Criar seletor de perfil no frontend.
- Implementar gerenciamento do perfil ativo.
- Implementar visão Geral.
- Adaptar a navegação.

Critério de conclusão:

A aplicação deverá permitir alternar entre os quatro perfis e a visão Geral, preservando os dados existentes.

### Etapa 4 — Ajustes nos registros e materiais-guia

Objetivo: generalizar o registro de atividades.

Atividades:

- Analisar entidades, DTOs, serviços e componentes de registros.
- Analisar os tipos de registro existentes.
- Definir novos tipos de registro.
- Definir regras de obrigatoriedade.
- Flexibilizar matéria e assunto.
- Definir referências a projetos e tarefas.
- Implementar validações condicionais.
- Adaptar formulários de criação e edição.
- Preservar o formulário de concursos.
- Implementar registros de tecnologia, trabalho e pesquisa.
- Adaptar o Histórico.
- Analisar materiais-guia e tipos de material.
- Ampliar o cadastro de materiais-guia.
- Reaproveitar os campos de ponto de parada e material utilizado.
- Investigar a sequência utilizada em materiais-guia.

Critério de conclusão:

O sistema deverá permitir registrar diferentes modalidades de atividades utilizando uma única tabela de registros.

A modelagem dos projetos deverá ser definida antecipadamente, mesmo que sua implementação completa ocorra na etapa seguinte.

### Etapa 5 — Ajustes no planejamento e projetos

Objetivo: ampliar as possibilidades de planejamento.

Atividades:

- Analisar toda a estrutura existente de planejamento.
- Preservar blocos, matérias, assuntos e etapas de estudo.
- Definir a modelagem definitiva de projetos.
- Implementar projetos, etapas e tarefas.
- Implementar associação entre projetos e planejamentos.
- Implementar associação entre materiais-guia e planejamentos.
- Integrar projetos e tarefas aos registros.
- Adaptar formulários de planejamento.
- Implementar planejamentos de estudos.
- Implementar planejamentos de projetos.
- Implementar planejamentos híbridos.
- Definir cálculos de progresso.
- Validar conclusão automática e manual das atividades.

Critério de conclusão:

O sistema deverá permitir organizar conhecimentos e projetos, isoladamente ou de maneira combinada, dentro dos planejamentos.

### Etapa 6 — Ajustes gerais, integração e publicação

Objetivo: consolidar a nova versão.

Atividades:

- Adaptar Dashboard.
- Adaptar Metas.
- Adaptar Ciclos.
- Adaptar Histórico.
- Avaliar Caminho.
- Avaliar Acompanhamento.
- Avaliar Metodologia.
- Adaptar Gerenciador de Dados.
- Revisar consultas SQL.
- Revisar índices.
- Revisar validações.
- Avaliar representação da duração dos registros.
- Atualizar documentação.
- Executar testes unitários e de integração.
- Executar testes de regressão.
- Validar migrations.
- Realizar homologação.
- Elaborar estratégia de backup e recuperação.
- Planejar integração da develop à master.
- Publicar a nova versão após aprovação.

Critério de conclusão:

O RFX Estudos 2.0 deverá estar funcional, testado e compatível com os dados e funcionalidades existentes.

## 13. Resumo das alterações preliminares no banco de dados

| Tabela | Operação proposta |
|---|---|
| categorias | Reaproveitar e ampliar |
| registros | Alterar |
| planejamentos | Alterar |
| materias | Preservar |
| assuntos | Preservar |
| tipos_registro | Reaproveitar e ampliar |
| material_tipos | Analisar e ampliar, se necessário |
| materiais_guia | Alterar |
| planejamento_grupos_materias | Preservar |
| planejamento_materias | Preservar |
| planejamento_grupo_assunto_itens | Preservar inicialmente |
| planejamento_item_etapas | Preservar |
| planejamento_materiais_guia | Criar, sujeito à validação |
| projetos | Criar, sujeito à validação |
| projeto_etapas | Criar, sujeito à validação |
| projeto_tarefas | Criar, sujeito à validação |
| planejamento_projetos | Criar, sujeito à validação |
| Tabelas de metas | Analisar |
| Tabelas de ciclos | Analisar |

## 14. Requisitos de segurança e compatibilidade

Durante o desenvolvimento:

- Não realizar alterações diretamente no banco de produção.
- Não executar comandos destrutivos sem autorização.
- Não remover tabelas ou colunas sem análise de impacto.
- Não alterar configurações de produção sem autorização.
- Não realizar merge automático na branch de produção.
- Não expor credenciais em arquivos versionados.
- Não utilizar dados sensíveis de produção desnecessariamente.
- Preservar identificadores e relacionamentos existentes.
- Validar migrations em ambiente local.
- Garantir possibilidade de recuperação em caso de falha.
- Preservar o funcionamento das funcionalidades atuais de concursos.

## 15. Solicitação ao Codex

Neste momento, não iniciar a implementação das funcionalidades.

Primeiramente, realizar uma análise técnica completa dos repositórios e elaborar um plano de implementação do RFX Estudos 2.0.

### 15.1. Levantamento da arquitetura atual

Identificar:

- Estrutura de diretórios.
- Versões das tecnologias utilizadas.
- Entidades JPA.
- Repositórios.
- Serviços.
- Controladores.
- DTOs.
- Mappers.
- Validações.
- Consultas SQL.
- Migrations existentes.
- Componentes Angular.
- Serviços Angular.
- Rotas.
- Modelos TypeScript.
- Configurações de ambiente.
- Configurações de deploy.
- Testes existentes.

### 15.2. Levantamento do banco de dados

Analisar todas as tabelas relevantes e seus relacionamentos.

Dar atenção especial a:

- Categorias.
- Matérias.
- Assuntos.
- Registros.
- Tipos de registro.
- Materiais-guia.
- Tipos de material.
- Planejamentos.
- Grupos de matérias.
- Grupos de assuntos.
- Itens de planejamento.
- Etapas.
- Metas.
- Ciclos.

Identificar diferenças entre as propostas deste documento e a implementação atual.

Caso não exista acesso ao banco ou ao DDL completo, informar essa limitação e identificar quais informações adicionais são necessárias.

### 15.3. Análise de impacto

Identificar:

- Entidades que precisarão ser alteradas.
- Novas entidades necessárias.
- Entidades que poderão ser reaproveitadas.
- Endpoints afetados.
- Consultas afetadas.
- Componentes afetados.
- Regras de negócio afetadas.
- Dependências entre módulos.
- Riscos de regressão.
- Riscos relacionados à migração dos dados.
- Possíveis inconsistências no modelo atual.

### 15.4. Validação das propostas arquiteturais

Avaliar criticamente:

- Reutilização de categorias como perfis.
- Centralização dos registros em uma única tabela.
- Flexibilização de matérias e assuntos.
- Associação de registros a projetos.
- Estrutura proposta de projetos, etapas e tarefas.
- Reutilização dos materiais-guia.
- Associação de materiais aos planejamentos.
- Planejamentos híbridos.
- Estratégia de cálculo de progresso.
- Estratégia de migração dos dados existentes.

Não assumir que todas as propostas deste documento são definitivas.

Caso exista uma alternativa mais simples ou mais compatível com a arquitetura atual, apresentá-la com justificativa.

### 15.5. Elaboração do planejamento técnico

Apresentar um planejamento organizado nas seis etapas definidas neste documento.

Para cada etapa, apresentar:

- Objetivo.
- Escopo.
- Épicos.
- Tarefas.
- Subtarefas.
- Dependências.
- Arquivos e componentes afetados.
- Alterações no banco de dados.
- Alterações no backend.
- Alterações no frontend.
- Estratégia de testes.
- Critérios de aceite.
- Riscos.
- Estimativa relativa de complexidade.

### 15.6. Entregáveis esperados

Ao final da análise, apresentar:

1. Diagnóstico da arquitetura atual.
2. Diagrama do modelo de dados atual.
3. Proposta de arquitetura para o RFX Estudos 2.0.
4. Diagrama do modelo de dados proposto.
5. Relação das tabelas mantidas, alteradas e criadas.
6. Estratégia de migrations.
7. Estratégia de compatibilidade com os dados existentes.
8. Roadmap técnico de implementação.
9. Backlog organizado em épicos e tarefas.
10. Lista de decisões arquiteturais pendentes.
11. Estratégia de testes e homologação.
12. Riscos identificados e respectivas medidas de mitigação.

A implementação deverá ocorrer somente após a revisão e aprovação do planejamento técnico.