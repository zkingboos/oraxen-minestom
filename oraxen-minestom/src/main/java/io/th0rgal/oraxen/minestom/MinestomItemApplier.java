package io.th0rgal.oraxen.minestom;

import io.th0rgal.oraxen.core.items.AttributeModifierEntry;
import io.th0rgal.oraxen.core.items.FoodComponent;
import io.th0rgal.oraxen.core.items.OraxenItem;
import net.kyori.adventure.key.Key;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlotGroup;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.component.Food;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.RegistryKey;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Applies neutral {@link OraxenItem} components onto a Minestom ItemStack builder.
 * Attribute/enchantment names are resolved from Bukkit-style names to Minecraft registry keys.
 */
final class MinestomItemApplier {

    private MinestomItemApplier() {
    }

    static void apply(ItemStack.Builder builder, OraxenItem item) {
        applyEnchantments(builder, item.enchantments());
        applyAttributeModifiers(builder, item.attributeModifiers());
        applyFood(builder, item.food());
        applyConsumable(builder, item.consumable());
        // TODO: potionContents — PotionType/custom color are not yet mapped.
    }

    private static void applyEnchantments(ItemStack.Builder builder, Map<String, Integer> enchantments) {
        if (enchantments == null || enchantments.isEmpty()) return;
        Map<RegistryKey<Enchantment>, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : enchantments.entrySet()) {
            RegistryKey<Enchantment> key = RegistryKey.unsafeOf("minecraft:" + e.getKey().toLowerCase(java.util.Locale.ROOT));
            map.put(key, e.getValue());
        }
        builder.set(DataComponents.ENCHANTMENTS, new EnchantmentList(map));
    }

    private static void applyAttributeModifiers(ItemStack.Builder builder,
                                                java.util.List<AttributeModifierEntry> modifiers) {
        if (modifiers == null || modifiers.isEmpty()) return;
        java.util.List<AttributeList.Modifier> list = new java.util.ArrayList<>();
        int i = 0;
        for (AttributeModifierEntry entry : modifiers) {
            Attribute attribute = Attribute.fromKey(attributeKey(entry.attribute()));
            if (attribute == null) continue;
            AttributeOperation op = AttributeOperation.fromId(entry.operation());
            EquipmentSlotGroup slot = slotGroup(entry.slot());
            Key id = Key.key("oraxen", "modifier_" + (i++));
            AttributeModifier modifier = new AttributeModifier(id, entry.amount(), op);
            list.add(new AttributeList.Modifier(attribute, modifier, slot));
        }
        if (!list.isEmpty()) {
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, new AttributeList(list));
        }
    }

    private static void applyFood(ItemStack.Builder builder, FoodComponent food) {
        if (food == null) return;
        // TODO: verify saturation semantic (config uses saturation points; Minestom expects a modifier).
        builder.set(DataComponents.FOOD, new Food(food.nutrition(), food.saturation(), food.canAlwaysEat()));
    }

    private static void applyConsumable(ItemStack.Builder builder,
                                        io.th0rgal.oraxen.core.items.ConsumableComponent consumable) {
        if (consumable == null) return;

        net.minestom.server.item.ItemAnimation animation = animation(consumable.animation());
        net.minestom.server.sound.SoundEvent sound = net.minestom.server.sound.SoundEvent.fromKey(soundKey(consumable.sound()));
        if (sound == null) {
            sound = net.minestom.server.sound.SoundEvent.fromKey("minecraft:entity.generic.eat");
        }

        java.util.List<net.minestom.server.item.component.ConsumeEffect> effects = new java.util.ArrayList<>();
        if (consumable.consumeEffects() != null) {
            for (io.th0rgal.oraxen.core.items.ConsumeEffectGroup group : consumable.consumeEffects()) {
                if ("apply_effects".equals(group.type()) && group.effects() != null) {
                    java.util.List<net.minestom.server.potion.CustomPotionEffect> potionEffects = new java.util.ArrayList<>();
                    for (java.util.Map.Entry<String, io.th0rgal.oraxen.core.items.PotionEffectEntry> e : group.effects().entrySet()) {
                        net.minestom.server.potion.PotionEffect pe = net.minestom.server.potion.PotionEffect.fromKey(e.getKey());
                        if (pe == null) continue;
                        io.th0rgal.oraxen.core.items.PotionEffectEntry s = e.getValue();
                        potionEffects.add(new net.minestom.server.potion.CustomPotionEffect(
                                pe, s.amplifier(), s.duration(), s.ambient(), s.showParticles(), s.showIcon()));
                    }
                    if (!potionEffects.isEmpty()) {
                        effects.add(new net.minestom.server.item.component.ConsumeEffect.ApplyEffects(
                                potionEffects, group.probability()));
                    }
                }
                // TODO: clear_all_effects, play_sound, teleport_randomly consume effect types.
            }
        }

        builder.set(DataComponents.CONSUMABLE,
                new net.minestom.server.item.component.Consumable(
                        consumable.consumeSeconds(), animation, sound, consumable.hasConsumeParticles(), effects));
    }

    private static net.minestom.server.item.ItemAnimation animation(String name) {
        if (name == null) return net.minestom.server.item.ItemAnimation.NONE;
        return switch (name.toLowerCase(java.util.Locale.ROOT)) {
            case "eat" -> net.minestom.server.item.ItemAnimation.EAT;
            case "drink" -> net.minestom.server.item.ItemAnimation.DRINK;
            default -> net.minestom.server.item.ItemAnimation.NONE;
        };
    }

    private static String soundKey(String sound) {
        return sound != null && sound.contains(":") ? sound : "minecraft:" + sound;
    }

    private static String attributeKey(String bukkitName) {
        String normalized = bukkitName.toLowerCase(java.util.Locale.ROOT).replace("generic_", "");
        return "minecraft:" + normalized;
    }

    private static EquipmentSlotGroup slotGroup(String slot) {
        if (slot == null) return EquipmentSlotGroup.MAIN_HAND;
        return switch (slot.toUpperCase(java.util.Locale.ROOT)) {
            case "HAND", "MAIN_HAND" -> EquipmentSlotGroup.MAIN_HAND;
            case "OFF_HAND" -> EquipmentSlotGroup.OFF_HAND;
            case "FEET" -> EquipmentSlotGroup.FEET;
            case "LEGS" -> EquipmentSlotGroup.LEGS;
            case "CHEST" -> EquipmentSlotGroup.CHEST;
            case "HEAD" -> EquipmentSlotGroup.HEAD;
            default -> EquipmentSlotGroup.MAIN_HAND;
        };
    }
}