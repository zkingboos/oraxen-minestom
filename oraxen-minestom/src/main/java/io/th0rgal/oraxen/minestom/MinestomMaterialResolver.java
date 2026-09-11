package io.th0rgal.oraxen.minestom;

import net.minestom.server.item.Material;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolve nomes de material Bukkit (ex: "DIAMOND_SWORD") para {@link Material} do Minestom.
 * Mantém cache para performance.
 */
public final class MinestomMaterialResolver {

    private static final Map<String, Material> CACHE = new ConcurrentHashMap<>();

    private MinestomMaterialResolver() {}

    /**
     * Resolve um nome de material estilo Bukkit (ex: "DIAMOND_SWORD", "PAPER") para Material do Minestom.
     * Aceita tanto "DIAMOND_SWORD" quanto "minecraft:diamond_sword".
     *
     * @param bukkitName nome do material no formato Bukkit/Paper
     * @return Material correspondente ou null se não mapeável
     */
    public static Material resolve(String bukkitName) {
        if (bukkitName == null || bukkitName.isBlank()) return null;

        // Se já contém namespace (minecraft:...), usa direto
        String key = bukkitName.toLowerCase(Locale.ROOT);
        if (key.contains(":")) {
            return Material.fromKey(key);
        }

        // Caso padrão: prefixa com minecraft:
        key = "minecraft:" + key;
        Material cached = CACHE.get(key);
        if (cached != null) return cached;

        Material material = Material.fromKey(key);
        if (material != null) {
            CACHE.put(key, material);
        }
        return material;
    }

    /**
     * Verifica se um nome de material pode ser resolvido (sem side effects).
     */
    public static boolean canResolve(String bukkitName) {
        return resolve(bukkitName) != null;
    }
}