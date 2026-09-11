package io.th0rgal.oraxen.core.items;

import java.util.Map;

/**
 * A consume effect group: type ("apply_effects", "clear_all_effects", ...),
 * a map of potion effect id -> settings, and a probability.
 */
public record ConsumeEffectGroup(
        String type,
        Map<String, PotionEffectEntry> effects,
        float probability
) {}