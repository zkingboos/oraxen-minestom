---
name: merge_upstream
description: Regras de merge com o upstream Oraxen e re-port obrigatório para Minestom
trigger: always_on
---

# Merge com o upstream Oraxen

## Regras estruturais
- Nunca desenvolver direto na `master`. Trabalhar na branch `minestom` (ou feature off dela).
- Atualmente só existe `origin` apontando para `oraxen/oraxen` (fork ficou adiado). Enquanto não houver fork, **não fazer `git pull` cego** sobre `origin`.
- Quando o fork existir: `git fetch upstream && git merge upstream/master` na branch `minestom`.

## Re-port obrigatório
- Toda mudança upstream que toca código já portado exige re-aplicar a adaptação em `oraxen-minestom` no **mesmo commit/PR**.
- Classificar `git diff --name-only upstream/master..HEAD` em:
  - (a) **puro** → reavaliar se deve ser extraído para `oraxen-core`;
  - (b) **bukkit-typed** → verificar se há espelho/porta em `oraxen-minestom`;
  - (c) **já portado** → re-aplicar a adaptação correspondente.

## Proibido
- Sobrescrever regressivamente o conteúdo de `oraxen-core` e `oraxen-minestom` com o merge.
- Mascarar pendência sem `// TODO:` explicando o que falta portar.