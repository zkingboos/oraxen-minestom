package io.th0rgal.oraxen.minestom;

import io.th0rgal.oraxen.core.items.AttributeModifierEntry;
import io.th0rgal.oraxen.core.items.ConsumableComponent;
import io.th0rgal.oraxen.core.items.ConsumeEffectGroup;
import io.th0rgal.oraxen.core.items.FoodComponent;
import io.th0rgal.oraxen.core.items.OraxenItem;
import io.th0rgal.oraxen.core.items.PotionEffectEntry;
import net.kyori.adventure.key.Key;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlotGroup;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.item.ItemAnimation;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.AttributeList;
import net.minestom.server.item.component.Consumable;
import net.minestom.server.item.component.ConsumeEffect;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.component.Food;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.CustomPotionEffect;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.sound.SoundEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
            RegistryKey<Enchantment> key = RegistryKey.unsafeOf("minecraft:" + e.getKey().toLowerCase(Locale.ROOT));
            map.put(key, e.getValue());
        }
        builder.set(DataComponents.ENCHANTMENTS, new EnchantmentList(map));
    }

    private static void applyAttributeModifiers(ItemStack.Builder builder, List<AttributeModifierEntry> modifiers) {
        if (modifiers == null || modifiers.isEmpty()) return;
        List<AttributeList.Modifier> list = new ArrayList<>();
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

    private static void applyConsumable(ItemStack.Builder builder, ConsumableComponent consumable) {
        if (consumable == null) return;

        ItemAnimation animation = animation(consumable.animation());
        SoundEvent sound = SoundEvent.fromKey(soundKey(consumable.sound()));
        if (sound == null) {
            sound = SoundEvent.fromKey("minecraft:entity.generic.eat");
        }

        List<ConsumeEffect> effects = new ArrayList<>();
        if (consumable.consumeEffects() != null) {
            for (ConsumeEffectGroup group : consumable.consumeEffects()) {
                if ("apply_effects".equals(group.type()) && group.effects() != null) {
                    List<CustomPotionEffect> potionEffects = new ArrayList<>();
                    for (Map.Entry<String, PotionEffectEntry> e : group.effects().entrySet()) {
                        PotionEffect pe = PotionEffect.fromKey(e.getKey());
                        if (pe == null) continue;
                        PotionEffectEntry s = e.getValue();
                        potionEffects.add(new CustomPotionEffect(
                                pe, s.amplifier(), s.duration(), s.ambient(), s.showParticles(), s.showIcon()));
                    }
                    if (!potionEffects.isEmpty()) {
                        effects.add(new ConsumeEffect.ApplyEffects(potionEffects, group.probability()));
                    }
                }
                // TODO: clear_all_effects, play_sound, teleport_randomly consume effect types.
            }
        }

        builder.set(DataComponents.CONSUMABLE,
                new Consumable(consumable.consumeSeconds(), animation, sound, consumable.hasConsumeParticles(), effects));
    }

    private static ItemAnimation animation(String name) {
        if (name == null) return ItemAnimation.NONE;
        return switch (name.toLowerCase(Locale.ROOT)) {
            case "eat" -> ItemAnimation.EAT;
            case "drink" -> ItemAnimation.DRINK;
            default -> ItemAnimation.NONE;
        };
    }

    private static String soundKey(String sound) {
        return sound != null && sound.contains(":") ? sound : "minecraft:" + sound;
    }

    private static String attributeKey(String bukkitName) {
        String normalized = bukkitName.toLowerCase(Locale.ROOT).replace("generic_", "");
        return "minecraft:" + normalized;
    }

    private static EquipmentSlotGroup slotGroup(String slot) {
        if (slot == null) return EquipmentSlotGroup.MAIN_HAND;
        return switch (slot.toUpperCase(Locale.ROOT)) {
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