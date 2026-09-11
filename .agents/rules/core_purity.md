---
name: core_purity
description: Proibições de dependência dentro de oraxen-core (nada de Bukkit/Minestom/NMS/pacotes)
trigger: path_scoped
patterns:
  - "oraxen-core/**"
---

# Pureza do oraxen-core

## Proibido em `oraxen-core`
Nenhum import destes grupos pode existir no `oraxen-core`:

- `org.bukkit`
- `net.minestom`
- `net.minecraft`
- `io.papermc.paper`
- `org.bukkit.craftbukkit`
- `com.comphenix` (ProtocolLib)
- `com.github.retrooper` (PacketEvents)

## Dependências permitidas
- **Adventure** (`net.kyori`) — declarado `compileOnly`, não `api`, para não conflitar com a versão embutida no Minestom.
- Dependência nova no core exige justificativa explícita no commit.

## Check obrigatório antes de commit
Este comando deve retornar vazio:

```bash
grep -R "org\.bukkit\|net\.minestom\|net\.minecraft\|io\.papermc\|com\.comphenix\|com\.github\.retrooper" oraxen-core/src
```

Se retornar algo, a classe não pertence ao core (mover/reverter).