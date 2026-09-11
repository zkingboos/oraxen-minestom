---
name: build_verification
description: Verificação obrigatória de build/teste antes de declarar tarefa concluída
trigger: always_on
---

# Verificação de Build e Teste

## Comandos obrigatórios antes de declarar "concluído"
```bash
./gradlew build                 # bukkit (root) + shadowJar
./gradlew :oraxen-minestom:build # módulo Minestom (quando existir)
./gradlew test                  # testes unitários
```

## Regras
- `compileJava.get().dependsOn(clean)` já existe; o `build` depende de `shadowJar`. Não reportar sucesso por compilação local apenas — anotar a saída.
- Teste de version-loading só com `-PrunVersionLoadingTest` (excluído por padrão).
- O gate de regressão (CP1) exige: `./gradlew build` verde **e** a API pública (`io.th0rgal.oraxen.api`) inalterada após adicionar `oraxen-core`.
- Check de pureza do core: `grep -R "org\.bukkit\|net\.minestom\|net\.minecraft" oraxen-core/src` deve ser vazio.

## Proibido
- Declarar concluído sem rodar os comandos acima e sem registrar a evidência no progress tracker.
- Ignorar falha de build/teste como "deve passar em outro ambiente".