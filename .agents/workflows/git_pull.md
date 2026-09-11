---
name: git_pull
description: Re-sincronizar com o upstream Oraxen e re-portar para Minestom
---

# Workflow: git_pull

Executar quando o usuário fizer `git pull` (ou pedir sync com o upstream).

## Passos

1. `git fetch upstream` e identificar o range a mesclar.
2. Merge na branch `main` da branch `upstream/master` (nunca `git pull` cego sobre `origin`, que é o fork `zkingboos/oraxen-minestom`).
3. `git diff --name-only upstream/master..HEAD` → classificar cada arquivo tocado em:
   - **(a) puro** → reavaliar extração para `oraxen-core` (ver `architecture_modules.md`);
   - **(b) bukkit-typed** → verificar se há espelho/porta em `oraxen-minestom`;
   - **(c) já portado** → re-aplicar a adaptação correspondente.
4. Re-aplicar as adaptações em `oraxen-minestom` no mesmo commit/PR (ver `merge_upstream.md`).
5. Verificar: `./gradlew build`, `./gradlew :oraxen-minestom:build`, `./gradlew test` (ver `build_verification.md`).
6. Atualizar `.agents/work/MINESTOM_PORT_STATUS.md` com: arquivos upstream tocados, o que foi re-portado, o que ficou `// TODO:`.