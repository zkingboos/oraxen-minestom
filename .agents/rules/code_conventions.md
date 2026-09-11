---
name: code_conventions
description: Convenções de código: return early, dead code, YAGNI, TODOs, idioma consistente
trigger: always_on
---

# Convenções de Código

## Estrutura
- **Return early / guard clauses**: tratar erro/saída/validação no início; proibido código pirâmide ("kamehameha").
- **Dead code**: remover funções, imports, branches sem chamador. Antes de concluir, buscar chamadores (`rg`). Nunca manter "por precaução" (YAGNI).
- **YAGNI**: sem abstração especulativa; interface com uma única implementação é suspeita; factory para um único produto é proibida.

## Pendências
- Funcionalidade/serviço ainda não suportado → `// TODO:` descrevendo o que falta. Proibido valor/mockado disfarçando a ausência.
- `// TODO:` só é removido quando a adaptação correspondente for implementada (ver `porting_mechanic.md`).

## Idioma e estilo
- Comentários em inglês (o codebase Oraxen é inglês), consistentes por arquivo.
- Seguir o estilo do arquivo vizinho; não introduzir nova lib sem justificativa no commit.