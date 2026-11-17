# Ideia: super matérias para revisão e questões

> Nota para discussão. Esta proposta ainda não foi implementada.

## Objetivo

Uma **super matéria** é uma composição salva de assuntos, usada principalmente em
planejamentos de revisão e de questões avançadas. Ela permite reunir assuntos de
uma ou várias matérias e reutilizar essa seleção em diferentes planejamentos.

Exemplo:

```text
Super matéria: Revisão de Funções
├── Matemática → Função afim         (peso 3)
├── Matemática → Função quadrática   (peso 5)
└── Matemática → Função exponencial  (peso 2)
```

Não existe uma terceira modalidade de planejamento chamada "combinação": a super
matéria é a combinação persistente e reutilizável. No planejamento, a origem será
uma matéria normal ou uma super matéria.

## Uso no planejamento

Ao selecionar uma super matéria, o planejamento considera apenas os assuntos que
fazem parte dela. O peso de cada item pode orientar a distribuição sugerida de
tempo, sessões ou quantidade de questões.

No exemplo acima, uma meta de 50 questões poderia sugerir:

```text
Função afim:        15 questões
Função quadrática:  25 questões
Função exponencial: 10 questões
```

Os pesos são uma sugestão de distribuição, não uma obrigação. O usuário poderá
ajustar os valores conforme a lista de questões ou sua necessidade de revisão.

## Registro de questões

O fato de diversos assuntos fazerem parte da super matéria **não** significa que
um registro de questões deve ser contabilizado integralmente para todos eles.
Isso duplicaria totais e produziria estatísticas incorretas.

Há dois modos de registro:

### Registro distribuído por assunto

Usado quando se conhece a divisão das questões por assunto. Uma mesma sessão pode
ter resultados separados para os assuntos participantes.

```text
Sessão: 50 questões — Revisão de Funções
├── Função afim:        15 feitas, 12 acertadas
├── Função quadrática:  25 feitas, 19 acertadas
└── Função exponencial: 10 feitas,  8 acertadas
```

Cada subtotal alimenta somente as estatísticas do respectivo assunto. A interface
deve iniciar essa divisão a partir dos pesos configurados, para reduzir o trabalho
manual.

### Registro geral da super matéria

Usado quando a lista é mista e não é possível determinar com confiança a qual
assunto cada questão pertence.

```text
Sessão: Revisão de Funções
50 feitas, 39 acertadas
```

Esse resultado é contabilizado no nível da super matéria ou do bloco de revisão,
mas não é repartido artificialmente entre os assuntos. Assim, os dados individuais
permanecem confiáveis.

## Princípios

- A super matéria agrega conteúdos; ela não substitui as matérias e assuntos de origem.
- O progresso individual só é atualizado quando houver uma distribuição conhecida por assunto.
- Totais de questões nunca podem ser duplicados ao incluir mais de um assunto na mesma sessão.
- O planejamento pode consolidar o progresso da super matéria a partir dos registros dos assuntos e dos registros gerais.
- Uma super matéria pode ser formada por assuntos da mesma matéria, o que é importante para revisões avançadas.

## Pontos para decidir depois

- Se uma super matéria poderá incluir uma matéria inteira ou somente assuntos explícitos.
- Se o peso será obrigatório, opcional com valor padrão `1`, ou se haverá outro critério de distribuição.
- Como apresentar separadamente, nos relatórios, as estatísticas por assunto e os registros gerais não distribuídos.
- Se o registro distribuído será armazenado em uma tabela filha do registro principal, com uma linha para cada assunto e seus totais.
