---
name: architecture_modules
description: Fronteiras dos 3 módulos (oraxen-core / oraxen-bukkit / oraxen-minestom) e regras de extração por mesmo FQCN
trigger: path_scoped
patterns:
  - "oraxen-core/**"
  - "oraxen-minestom/**"
  - "build.gradle.kts"
  - "settings.gradle.kts"
  - "gradle/**"
  - "nms/**"
---

# Arquitetura de Módulos (port Minestom)

## Os 3 módulos
- **`oraxen-core`** — módulo neutro, sem dependência de servidor. Contém lógica pura + contratos (scheduler, evento, item, config spine).
- **root (oraxen-bukkit)** — o plugin Oraxen atual (Paper/Folia). Depende de `oraxen-core`.
- **`oraxen-minestom`** — servidor Minestom próprio (build com `main()`, NÃO um plugin/Extension de terceiros). Depende de `oraxen-core`.

## Regra de extração (mesmo FQCN)
- Classes extraídas para `oraxen-core` **mantêm o mesmo FQCN** (`io.th0rgal.oraxen.*`). Java permite o mesmo pacote em jars diferentes (sem JPMS).
- Ao mover uma classe para o core, **remover a original do bukkit** no mesmo commit — nunca manter a classe duplicada nos dois jars.
- `oraxen-core` nunca pode quebrar a API pública existente do plugin (`io.th0rgal.oraxen.api`, `OraxenItems`, eventos públicos).

## Onde colocar cada coisa
- Lógica pura (pack generation, parser, schema, string/version parsing) → `oraxen-core`.
- Lógica que toca `org.bukkit`/`io.papermc`/`net.minecraft` → root (bukkit).
- Lógica que toca `net.minestom.server` → `oraxen-minestom`.

## Packaging
- `oraxen-minestom` = servidor próprio: `main()` chamando `MinecraftServer.init()`, manifest `Main-Class`, `shadowJar` fat-jar.
- `oraxen-core` é dependência `implementation(project(":oraxen-core"))` tanto do root quanto do minestom.