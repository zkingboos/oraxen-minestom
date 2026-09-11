---
name: merge_upstream
description: Regras de merge com o upstream Oraxen e re-port obrigatório para Minestom
trigger: always_on
---

# Merge com o upstream Oraxen

## Remotes (atual)
- **`origin`** → fork `zkingboos/oraxen-minestom` (branch padrão `main`).
- **`upstream`** → `oraxen/oraxen` (branch padrão `master`).

## Regras estruturais
- Nunca desenvolver direto na `main` do fork. Trabalhar em feature branch off de `main` (ou na `main` apenas para receber merge do upstream).
- Sync com o upstream: `git fetch upstream && git merge upstream/master` na branch `main`.
- **Não fazer `git pull` cego** sobre `origin` (que é o seu fork); o pull #de upstream# é sempre via `upstream/master`.

## Re-port obrigatório
- Toda mudança upstream que toca código já portado exige re-aplicar a adaptação em `oraxen-minestom` no **mesmo commit/PR**.
- Classificar `git diff --name-only upstream/master..HEAD` em:
  - (a) **puro** → reavaliar se deve ser extraído para `oraxen-core`;
  - (b) **bukkit-typed** → verificar se há espelho/porta em `oraxen-minestom`;
  - (c) **já portado** → re-aplicar a adaptação correspondente.

## Proibido
- Sobrescrever regressivamente o conteúdo de `oraxen-core` e `oraxen-minestom` com o merge.
- Mascarar pendência sem `// TODO:` explicando o que falta portar.