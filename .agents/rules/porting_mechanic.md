---
name: porting_mechanic
description: Critérios de paridade e proibições de degradação silenciosa ao portar mecanismos para Minestom
trigger: path_scoped
patterns:
  - "oraxen-minestom/**"
---

# Portabilidade de Mecanismos para Minestom

## Definição de "portado" (DoD de paridade)
Um mecanismo só é considerado portado quando `oraxen-minestom` reproduz o comportamento do `oraxen-bukkit` **e** tem um check executável (boot-test/self-check) que falha se o comportamento quebrar.

## Proibido
- Fallback silencioso degradado disfarçado de "funciona" (ex: hardness sem breaker, text overlay sem listener). Degradações conhecidas devem ser sinalizadas com `// TODO:` explícito, nunca implementação vazia.
- Implementar um mecanismo pela metade e declarar concluído sem o check executável correspondente.

## Ordem de prioridade de port
1. Items (ItemBuilder, NBT, modelo)
2. Resource pack (geração + envio)
3. Comandos (`/oraxen give` etc.)
4. Blocks
5. Furniture
6. Packets/NMS (hardness, text displays, efficiency, scoreboard/title)