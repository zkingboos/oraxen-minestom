package io.th0rgal.oraxen.core.items;

import java.util.List;
import java.util.Map;

/**
 * Consumable component for items that can be consumed.
 */
public record ConsumableComponent(
        float consumeSeconds,
        String animation,
        String sound,
        boolean hasConsumeParticles,
        List<ConsumeEffectGroup> consumeEffects
) {}