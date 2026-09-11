---
name: repo_hygiene
description: Higiene do repositório: não versionar binários nem artefatos; revisar antes de commit
trigger: always_on
---

# Higiene do Repositório

## Proibido versionar
- Binários (`*.jar`, `*.class`), diretórios de build (`build/`, `oraxen-core/build/`, `oraxen-minestom/build/`, `nms/**/build/`), `libs/` gerados, logs, dumps, secrets, `.env` reais.

## Revisão obrigatória antes de commit
- `git status` e `git diff --stat` para revisar o que entra.
- `.gitignore` deve cobrir os diretórios de build de todos os módulos (`oraxen-core`, `oraxen-minestom`, `nms/**`).

## Scripts e artefatos temporários
- Scripts auxiliares criados durante o desenvolvimento devem ser apagados após o uso, exceto se persistidos com justificativa explícita.