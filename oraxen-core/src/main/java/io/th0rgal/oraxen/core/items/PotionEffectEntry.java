package io.th0rgal.oraxen.core.items;

/**
 * A single potion effect applied by a consume effect.
 */
public record PotionEffectEntry(
        int duration,
        int amplifier,
        boolean ambient,
        boolean showParticles,
        boolean showIcon
) {}