# Visão futura: refatoração, ambiente local e Docker

> Documento de discussão criado em 17 de novembro de 2026.
> Este roteiro não autoriza alterações no código, no banco de dados ou na infraestrutura.
> O objetivo é preservar ideias para uma modernização futura e incremental.

## Objetivo

Melhorar a organização e a segurança da aplicação sem interromper o foco atual no módulo de concursos.

As frentes inicialmente imaginadas foram:

1. revisar e melhorar a arquitetura do backend;
2. revisar e melhorar a arquitetura do frontend;
3. criar um banco local reproduzível e separado da produção;
4. avaliar Docker e alternativas de hospedagem;
5. verificar se a arquitetura escolhida reduz custos operacionais.

A ordem recomendada é um pouco diferente: primeiro proteger as credenciais e criar um ambiente local confiável; depois iniciar as refatorações.

## Situação observada

O backend possui aproximadamente 5 mil linhas Java e ainda está em um tamanho administrável.

Os maiores pontos de concentração identificados são:

```text
PlanejamentoService       aproximadamente 864 linhas
PlanejamentoController    aproximadamente 595 linhas
```

Atualmente existem poucos testes automatizados e as alterações de banco estão distribuídas entre diretórios de scripts manuais e scripts SQL.

O repositório já possui um `Dockerfile`, mas não possui uma configuração de Docker Compose para o banco local.

O arquivo de configuração atual contém credenciais do banco remoto como valores padrão. Essas credenciais devem ser consideradas expostas, rotacionadas no provedor e removidas do código antes das demais fases.

## Princípios da modernização

- Manter o sistema funcionando ao final de cada etapa.
- Evitar uma refatoração total em uma única entrega.
- Criar testes antes de alterar regras importantes.
- Não usar o banco de produção durante o desenvolvimento local.
- Separar configuração de código e segredos.
- Fazer mudanças por responsabilidade, não apenas para reduzir o tamanho dos arquivos.
- Medir custos reais antes de trocar a arquitetura de produção.

## Fase 0 — Segurança e configuração

Esta fase deve preceder as demais.

Atividades futuras:

- trocar a senha atual do banco remoto;
- remover usuário e senha reais do arquivo versionado;
- verificar se o segredo permanece no histórico Git;
- usar variáveis de ambiente em produção;
- criar um `.env.example` sem valores sensíveis;
- impedir que a aplicação local se conecte silenciosamente à produção.

Configuração principal esperada:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

Os valores reais não devem ser gravados no repositório.

## Fase 1 — Ambiente local reproduzível

Esta fase deve acontecer antes da refatoração para permitir testes seguros.

A combinação inicialmente recomendada é:

```text
Backend executado pela IDE
PostgreSQL executado pelo Docker Compose
```

Não é necessário executar o backend em Docker durante o desenvolvimento. Manter somente o banco em contêiner permite reiniciar e depurar o Java diretamente pela IDE.

Arquivos que poderão ser criados:

```text
compose.yaml
.env.example
src/main/resources/application.properties
src/main/resources/application-local.properties
src/main/resources/application-prod.properties
```

### Banco local

O PostgreSQL local poderá usar um volume nomeado:

```text
postgres-local
└── volume: rfx_estudos_postgres_data
```

Esse volume:

- persiste os dados após a parada do contêiner;
- pertence apenas ao ambiente local;
- não compartilha dados com produção;
- pode ser recriado quando for necessário começar com uma base limpa.

### Perfis Spring

Separação esperada:

```text
local   PostgreSQL executado pelo Docker Compose
prod    banco remoto configurado por variáveis de ambiente
test    banco isolado para testes automatizados
```

Nenhum perfil local deve possuir uma URL remota como fallback.

## Fase 2 — Migrações de banco

Antes de adotar uma ferramenta automática, será necessário reconciliar o histórico existente.

Atualmente há scripts em:

```text
src/main/resources/db/migration-manual
src/main/resources/sql
```

Atividades futuras:

1. comparar o esquema real de produção com as entidades;
2. identificar scripts repetidos, substituídos ou incompatíveis;
3. definir uma base inicial capaz de criar um banco vazio;
4. escolher a estratégia de versionamento;
5. adotar Flyway;
6. validar a marcação das migrações já aplicadas em produção;
7. testar a criação completa de um banco local vazio.

O Flyway não deve ser ativado diretamente em produção antes dessa reconciliação.

## Fase 3 — Proteção por testes

Antes de refatorar os fluxos principais, criar testes de caracterização que descrevam o comportamento atual.

Prioridades:

- criação e atualização de planejamento;
- matérias e grupos de matérias;
- grupos de assuntos e itens;
- etapas e conclusão automática;
- registros;
- ciclos;
- metas;
- validações entre matéria e assunto.

Tipos de teste possíveis:

```text
testes unitários de serviços
testes de controllers
testes de integração com PostgreSQL
Testcontainers para cenários que dependem do banco
```

A sequência para cada mudança deve ser:

```text
mapear comportamento
→ criar ou ajustar testes
→ refatorar
→ executar testes
→ revisar
```

## Fase 4 — Auditoria e refatoração do backend

A auditoria deverá procurar:

- validações de IDs e relacionamentos repetidas;
- chamadas repetidas aos mesmos repositórios;
- montagem manual repetida de DTOs;
- tratamento inconsistente de recursos não encontrados;
- regras de negócio dentro de controllers;
- métodos longos;
- consultas N+1;
- nomes inconsistentes;
- código não utilizado;
- endpoints com comportamento equivalente;
- responsabilidades misturadas.

### Planejamento

Os primeiros candidatos à decomposição são `PlanejamentoService` e `PlanejamentoController`.

Possível divisão por responsabilidade:

```text
PlanejamentoService
├── PlanejamentoMateriaService
├── PlanejamentoGrupoMateriaService
├── PlanejamentoAssuntoService
└── PlanejamentoEtapaService
```

A divisão definitiva deve ser baseada nas regras encontradas, não somente no número de linhas.

### Melhorias transversais possíveis

- DTOs específicos para entrada e saída;
- validação de entrada centralizada;
- tratamento de erros com `@ControllerAdvice`;
- mapeamento consistente entre entidades e DTOs;
- transações definidas na camada de serviço;
- padronização de nomes e retornos HTTP;
- consultas explícitas para telas de resumo;
- análise estática e detecção de duplicação.

A reorganização de pacotes deve ser gradual. Uma alteração completa de todas as classes em uma única entrega aumenta o risco sem necessariamente melhorar a arquitetura.

## Fase 5 — Auditoria e refatoração do frontend

O frontend está em outro projeto e precisará ser analisado separadamente.

A auditoria deverá procurar:

- chamadas HTTP repetidas;
- serviços que consultam os mesmos endpoints;
- formulários duplicados;
- componentes muito grandes;
- regras de negócio em templates;
- conversões repetidas de datas e horários;
- interfaces equivalentes com nomes diferentes;
- tabelas, diálogos e mensagens repetidos;
- estados de carregamento e erro inconsistentes;
- métodos existentes que foram reimplementados;
- assinaturas e inscrições não encerradas;
- dependências não utilizadas.

Uma organização possível por funcionalidades:

```text
planejamento/
registros/
ciclos/
materias/
metas/
core/
shared/
```

`shared` deve conter apenas elementos realmente reutilizáveis. Regras próprias de uma funcionalidade devem permanecer dentro dela.

A refatoração do frontend também deverá ser incremental:

```text
mapear fluxo
→ criar ou ajustar testes
→ extrair responsabilidade
→ validar comportamento
→ validar interface visualmente
```

## Fase 6 — Docker para desenvolvimento e produção

### Desenvolvimento

Uso recomendado inicialmente:

```text
Docker Compose
└── PostgreSQL local

IDE
└── Backend Spring Boot
```

Posteriormente poderá existir um perfil que execute backend e banco juntos, útil para validação ou integração, sem substituir obrigatoriamente o fluxo da IDE.

### Produção

O backend já possui um `Dockerfile`, mas ele poderá ser revisado para:

- aproveitar cache das dependências Maven;
- executar testes antes de produzir a imagem;
- usar uma imagem compatível com a versão do Java e do Spring Boot;
- executar com usuário não privilegiado;
- adicionar health check;
- definir limites adequados de memória da JVM;
- criar um `.dockerignore`;
- reduzir o conteúdo copiado para a imagem;
- manter a imagem final pequena.

Docker padroniza a execução, mas não reduz custos automaticamente.

## Fase 7 — Avaliação de arquitetura e custos

Arquitetura inicial a ser considerada:

```text
Frontend estático
        ↓
Backend Spring Boot em contêiner
        ↓
PostgreSQL gerenciado
```

O custo depende de:

- memória reservada;
- CPU;
- tempo em execução;
- possibilidade de suspensão quando ocioso;
- tráfego;
- armazenamento;
- banco de dados;
- backups;
- observabilidade.

Executar PostgreSQL no mesmo servidor do backend pode parecer mais barato, mas transfere responsabilidades importantes:

- backups;
- atualizações;
- recuperação de falhas;
- persistência dos volumes;
- segurança;
- monitoramento;
- indisponibilidade do servidor.

Para uma aplicação pessoal, o banco gerenciado tende a reduzir risco operacional. A decisão final deve comparar preços e limites vigentes no momento da mudança, além do consumo real da aplicação.

## Sequência sugerida

```text
0. Rotacionar e remover credenciais expostas
1. Criar PostgreSQL local com Docker Compose
2. Separar perfis local, teste e produção
3. Reconciliar e automatizar migrações
4. Criar testes dos fluxos críticos
5. Auditar e refatorar o backend incrementalmente
6. Auditar e refatorar o frontend incrementalmente
7. Revisar Dockerfile e imagem de produção
8. Medir consumo e comparar hospedagens
9. Decidir a arquitetura de produção
```

## Entregáveis possíveis por fase

| Fase | Entregável |
| --- | --- |
| Segurança | configuração sem segredos e credenciais rotacionadas |
| Ambiente local | banco local iniciado por um comando |
| Migrações | banco vazio criado automaticamente |
| Testes | proteção dos principais comportamentos |
| Backend | relatório de duplicações e refatorações pequenas |
| Frontend | relatório de duplicações e componentes reorganizados |
| Docker | imagem reproduzível, segura e testada |
| Custos | comparação de opções com consumo e preços atuais |

## Decisões em aberto

- Qual versão do PostgreSQL deve ser usada localmente para corresponder à produção.
- Como transformar os scripts atuais em uma linha de migração confiável.
- Se o backend local será iniciado apenas pela IDE ou também pelo Compose.
- Qual nível de cobertura de testes é suficiente antes da refatoração.
- Se a organização do backend continuará por camadas ou migrará gradualmente por funcionalidade.
- Onde está hospedado o frontend e quais são seus custos atuais.
- Quais requisitos de disponibilidade e backup são realmente necessários.
- Se a aplicação precisa ficar ligada continuamente ou pode suspender quando ociosa.
- Se o banco continuará gerenciado ou será operado em infraestrutura própria.

## Fora do escopo atual

Este documento não executa nenhuma mudança e não define uma decisão definitiva de arquitetura. Ele registra um roteiro para discussão futura enquanto o desenvolvimento funcional continua.

