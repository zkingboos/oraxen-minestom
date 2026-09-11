# Config Spine — Avaliação e Decisão (T8)

Data: 2026-09-11. Fonte da verdade: `EXECUTION_PLAN.md`, `MINESTOM_PORT_STATUS.md`.

## O que é "config spine"

A camada de leitura/config que o Oraxen usa em **156 arquivos** via o tipo-esponja
`org.bukkit.configuration.ConfigurationSection` (e `YamlConfiguration`/`OraxenYaml`).
É o que a T8 chamou de "config spine dobrado no item".

## Achado técnico central

A API de config do Bukkit é, na prática, um **wrapper fino sobre SnakeYAML**: o modelo de
dados real é `Map<String, Object>`. O que *é* tipado em Bukkit dentro dessa camada é pouca
coisa:

- `OraxenYaml.getMaterial(String)` → `org.bukkit.Material` (enum de ~3000 entries).
- `ConfigsManager`/`ResourcesManager` → `JavaPlugin` (`getResource`, `saveResource`, `getDataFolder`).
- Alguns getters tipados (`getColor` → `org.bukkit.Color`, `getVector` → `org.bukkit.util.Vector`,
  serialization). O grosso (getString/getInt/getBoolean/getKeys/getSection/getList) é neutro.

Conclusão: **o "config" já é quase neutro** — o que não é neutro é (a) o enum `Material` e
(b) o acesso a recursos do JAR, não a leitura de YAML em si.

## O que já foi feito

O modelo **compartilhado entre plataformas é `OraxenItem`** (dados), não a leitura de config.
Cada plataforma mapeia config → `OraxenItem` do seu jeito:
- Bukkit: `OraxenYaml`/`ConfigsManager` → `ItemBuilder` (não extraído).
- Minestom: `MinestomItemLoader` (SnakeYAML direto) → `OraxenItem`.

## Opções (custo × benefício)

| # | Opção | Esforço | Valor agora | Risco |
|---|---|---|---|---|
| **A** | **Adiar** — manter adaptadores por plataforma | zero | — | nenhum |
| **B** | Interface neutra `ConfigSection` no core + impl SnakeYAML; Minestom usa, Bukkit adapta depois | médio | baixo hoje | interface com 1 consumidor (YAGNI) |
| **C** | Reescrever os 156 usos de `ConfigurationSection` p/ tipo neutro | muito alto (meses) | só no fim | alto (nunca termina) |

## Recomendação: Opção A (adiar)

- O ponto de compartilhamento já é o **modelo de dados `OraxenItem`** (feito).
- A leitura de config é **intencionalmente específica por plataforma hoje**: Bukkit usa a
  stack dele (compilado/lançado), Minestom usa SnakeYAML direto. Não há segundo consumidor
  real para justificar uma abstração neutra agora (viola YAGNI).
- **Quando revisitar (gatilho)**: no momento em que o Minestom passar a parsear *mechanics*
  ou *blocks* (config mais rico e compartilhado de verdade). Aí extrai-se para o core a
  **parsing de item/mechanic**, não o `ConfigurationSection` inteiro — expondo só os campos
  neutros que `OraxenItem` já delimita.

## Impacto no plano

T8 está **funcionalmente completa** para o escopo "items-only" do seed. O "config spine"
vira uma nova tarefa (T8b ou pós-T8), disparada pelo gatilho acima, não um bloqueio.