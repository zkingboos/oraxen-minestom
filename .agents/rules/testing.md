---
name: testing
description: Regras de testes: um check executável mínimo por lógica não-trivial; bugfix prova antes
trigger: always_on
---

# Testes

## Regra fundamental
- Toda lógica não-trivial (branch, loop, parser, caminho de dinheiro/segurança/estado) deixa **um check executável** — self-check (`main`/assert-based) ou um `test_*.java`/JUnit. Sem frameworks além do já usado (`JUnit 5`, `Mockito`) salvo pedido explícito.
- Bugfix de mecanismo portado inclui uma prova que falharia antes da correção.

## Política de não-regressão
- Cobertura comportamental não pode ser removida ou enfraquecida silenciosamente. Substituição/remoção de teste deve ser justificada no commit.
- Ignorar/`@Disabled` só temporariamente, com motivo e condição de remoção; não pode mascarar regressão.

## Regras adicionais
- `./gradlew test` sem `FAIL` e sem skip novo não justificado.
- A evidência do teste deve corresponder ao commit final exato.