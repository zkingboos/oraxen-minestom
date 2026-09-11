package io.th0rgal.oraxen.core.items;

/**
 * Attribute modifier entry for items.
 */
public record AttributeModifierEntry(
        String attribute,
        double amount,
        int operation,
        String slot
) {}