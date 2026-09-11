# Minestom Port — Status

Log vivo de progresso. Fonte da verdade do plano: `EXECUTION_PLAN.md`.

## Metadata
- **Base upstream SHA**: `83e87161` (master)
- **Branch de trabalho**: `minestom`
- **Início**: 2026-09-07

## Tarefas

| id | task | status | blocker | depende | último avanço |
|---|---|---|---|---|---|
| T0 | Rules + workflow + docs | completed | — | — | 13 arquivos criados |
| T1 | Scaffold oraxen-core | completed | B2 | T0 | settings.gradle + build.gradle + dep |
| T2 | Gate regressão (CP1) | completed | B3 | T1 | build verde + API intacta |
| T3 | Minestom dep + JDK25 (CP2) | completed | B1 | T1 | net.minestom:minestom:2025.12.20-1.21.11 + Java 25 |
| T4 | Extrair núcleo puro (CP3) | completed | — | T2 | Box.java extraído (same-FQCN) |
| T5 | Scheduler neutro (CP4) | completed | — | T4 | OraxenScheduler + BukkitSchedulerAdapter |
| T6 | Seed oraxen-minestom (CP5) | completed | — | T3,T5 | servidor próprio, items loader, /oraxen give |
| T7 | Eventos neutros | completed* | — | T4 | Event bus removido (quebrado/especulativo) |
| T8 | Item + config spine (CP7) | completed | — | T5,T7 | modelo neutro + loader + aplicação de componentes; config spine decidido (adiar — ver CONFIG_SPINE_DECISION.md) |
| T9 | Merge upstream + fork | deferred | — | — | adiado |

*Event bus removido por ser especulativo/quebrado; eventos reais serão portados per-mechanic.

## Bloqueadores
- **B1 (Minestom)**: resolvido — Maven Central (`net.minestom:minestom:2025.12.20-1.21.11`), toolchain Java 25.
- **B2/B3 (scaffold + gate)**: resolvidos.
- **Iris**: desativado (JitPack não publica artefatos); TODOs nos 2 arquivos `.disabled` + compatibilities + versions.toml.

## Log
- 2026-09-07: T0-T6 completados. Build verde. Iris desativado (TODOs). Iniciando T8.
- 2026-09-08: T8 — modelo neutro `OraxenItem` (record+builder, 25 campos) + componentes (AttributeModifierEntry, FoodComponent, ConsumableComponent, PotionContentsComponent, ToolComponent) no core; `MinestomItemLoader` (SnakeYAML) parseia id/name/material/Pack/Enchantments/AttributeModifiers/lore/max_damage/max_stack_size/unbreakable/item_model/damage/food; `MinestomMaterialResolver`; `MinestomItemApplier` aplica enchantments (RegistryKey.unsafeOf), attribute modifiers (Attribute.fromKey + op/slot), food. Servidor aplica MiniMessage name/lore, max stack, unbreakable, damage, custom model data, NBT marker. Porta configurável via `-Dminestom.port`. Boot test: 33 itens carregados, sem exceção. Build+test+purity verdes.
- 2026-09-08b: corrigidos bugs silenciosos — itemname MiniMessage usado (antes ignorado), /oraxen give id real (antes espada hardcoded), event bus especulativo removido, Builder limpo (campos mortos removidos).
- 2026-09-11: T8 — consumable completo (consume_seconds/animation/sound/particles + on_consume_effects `apply_effects` → CustomPotionEffect via PotionEffect.fromKey); ConsumeEffectGroup/PotionEffectEntry no core. `clear_all_effects`/`play_sound`/`teleport_randomly` e `potionContents` ficam `// TODO`. Boot: 33 itens, sem exceção. Build+test+purity verdes.

## Próximo passo
T8 concluído. Próximos passos (opcionais): T8b (config spine neutro, quando Minestom parsear mechanics/blocks) ou T9 (fork upstream + merge, adiado pelo usuário).