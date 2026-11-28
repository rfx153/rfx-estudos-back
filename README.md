# RFX Estudos — Backend

Backend Spring Boot com PostgreSQL local executado pelo Docker Compose e migrações de banco gerenciadas pelo Flyway.

## Pré-requisitos

- Java 17;
- Git;
- Docker com Docker Compose.

No Windows com WSL2, o Docker Desktop deve estar aberto e com a integração da distribuição Linux habilitada.

## Primeira execução

Clone o repositório, acesse a branch de desenvolvimento e entre na pasta do projeto:

```bash
git clone <url-do-repositorio>
cd rfx-estudos-back
git checkout develop
```

Crie o arquivo local de variáveis de ambiente:

```bash
cp .env.example .env
```

Inicie o PostgreSQL:

```bash
docker compose up -d
```

Confirme que o banco está saudável:

```bash
docker compose ps
```

O serviço `postgres` deve aparecer como `healthy`.

Inicie o backend:

```bash
bash mvnw spring-boot:run
```

Na primeira inicialização, o Flyway executa automaticamente a migration `V1__estrutura_inicial.sql` e cria as tabelas no banco vazio. Nas inicializações seguintes, ele apenas valida se o banco está atualizado.

Os logs devem indicar:

```text
The following 1 profile is active: "dev"
Current version of schema "public": 1
Schema "public" is up to date
```

Para verificar o backend:

```bash
curl -i http://localhost:8080/api/ping
```

O endpoint deve responder com o status HTTP `204`.

## Execuções seguintes

Depois da primeira configuração, normalmente bastam estes comandos:

```bash
docker compose up -d
bash mvnw spring-boot:run
```

O backend também pode ser iniciado pela IDE executando `rfx_estudos.app.AppApplication`. Nesse caso, configure o diretório de trabalho como a raiz do repositório para que o arquivo `.env` seja encontrado.

## Parar o ambiente

Interrompa o backend com `Ctrl+C`.

Para parar o PostgreSQL:

```bash
docker compose down
```

O volume nomeado preserva os dados para a próxima execução.

Não use o comando abaixo se quiser manter o banco local:

```bash
docker compose down -v
```

A opção `-v` remove o volume e apaga todos os dados locais. Na próxima subida, o Flyway criará novamente uma estrutura vazia.

## Configuração de desenvolvimento

O arquivo `.env` não é versionado. O modelo versionado está em `.env.example` e configura:

```text
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/rfx_estudos
SPRING_DATASOURCE_USERNAME=rfx_estudos
SPRING_DATASOURCE_PASSWORD=rfx_estudos
SPRING_FLYWAY_ENABLED=true
```

O banco executado pelo Compose usa:

```text
Banco: rfx_estudos
Usuário: rfx_estudos
Porta: 5432
```

## Migrações

As migrations ficam em:

```text
src/main/resources/db/migration
```

A migration `V1__estrutura_inicial.sql` não deve ser alterada depois de aplicada. Toda mudança futura de estrutura deve ser adicionada em um novo arquivo:

```text
V2__descricao_da_alteracao.sql
V3__outra_alteracao.sql
```

A cópia manual dos dados da aplicação em produção existe somente no volume desta máquina. Em uma instalação nova, o Flyway cria a estrutura e os dados de referência iniciais, mas não copia dados da produção.
