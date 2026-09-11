# Plano de Execução — Port Minestom do Oraxen

Documento fonte da verdade. O log vivo fica em `MINESTOM_PORT_STATUS.md`.

## Decisões tomadas (não reabrir sem nova decisão)
- **3 módulos**: `oraxen-core` (neutro) / root = `oraxen-bukkit` (Paper/Folia) / `oraxen-minestom` (servidor próprio).
- **`oraxen-minestom` = build de servidor próprio** (`main()` + `MinecraftServer.init()`), NÃO um plugin/Extension de terceiros.
- **Escopo do core (primeira iteração)**: máximo = pack + scheduler + evento + item (config spine dobrado no item).
- **Extração por mesmo FQCN** (`io.th0rgal.oraxen.*`), sem JPMS; remover a original do bukkit no mesmo commit.
- **Fork criado**: `origin` → `zkingboos/oraxen-minestom` (branch `main`); `upstream` → `oraxen/oraxen`.
- **Sem `agent_collaboration`**: progresso num único arquivo canônico.
- **Licença sinalizada**, sem decisão: fork Minestom redistribuído pode esbarrar na licença do Oraxen.

## Ordem de execução com checkpoints

| # | Task | CP | Critério de passagem |
|---|---|---|---|
| T0 | Rules + workflow + docs de tracking | CP0 | Arquivos existem e estão legíveis |
| T1 | Scaffold `oraxen-core` (settings + build + root depende) | — | Compila |
| T2 | Gate de regressão | CP1 | `./gradlew build` verde + API pública intacta |
| T3 | Minestom (`net.minestom:minestom`, Java 25) + Adventure compileOnly | CP2 | Dependência resolve + módulo compila |
| T4 | Extrair núcleo puro p/ core | CP3 | build+test verde |
| T5 | Contrato Scheduler neutro + impl Bukkit | CP4 | build+test verde |
| T6 | Seed `oraxen-minestom` (servidor próprio, items-only) | CP5 | boot headless + `/oraxen give` correto |
| T7 | Contrato de eventos neutro | CP6 | build+test verde |
| T8 | Contrato de Item + config spine | CP7 | build+test verde |
| T9 | Merge upstream + fork + re-port | — | adiado |

## Bloqueadores
- **B1 (Minestom)**: repo = Maven Central, coordenada `net.minestom:minestom` (confirmado). Falta nº de versão + JDK 25 disponível (T3).
- **B2/B3 (scaffold + gate)**: destravam tudo; T1/T2.

## DoD de paridade
Mecanismo "portado" = `oraxen-minestom` reproduz o comportamento do `oraxen-bukkit` **e** tem check executável que falha se quebrar.
Seed (T6): `/oraxen give <id>` → item com itemname/NBT/model corretos + pack aplicado no cliente.

## Comandos obrigatórios (ver `build_verification.md`)
```bash
./gradlew build
./gradlew :oraxen-minestom:build
./gradlew test
grep -R "org\.bukkit\|net\.minestom\|net\.minecraft" oraxen-core/src  # deve ser vazio
```