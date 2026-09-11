package io.th0rgal.oraxen.core.items;

/**
 * Food component for edible items.
 */
public record FoodComponent(
        int nutrition,
        float saturation,
        boolean canAlwaysEat
) {}