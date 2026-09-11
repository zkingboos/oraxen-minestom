package io.th0rgal.oraxen.minestom;

import io.th0rgal.oraxen.core.items.AttributeModifierEntry;
import io.th0rgal.oraxen.core.items.ConsumableComponent;
import io.th0rgal.oraxen.core.items.ConsumeEffectGroup;
import io.th0rgal.oraxen.core.items.FoodComponent;
import io.th0rgal.oraxen.core.items.OraxenItem;
import io.th0rgal.oraxen.core.items.PotionEffectEntry;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Loads Oraxen item definitions from items/*.yml into the neutral {@link OraxenItem} model.
 */
public final class MinestomItemLoader {

    private MinestomItemLoader() {
    }

    public static Map<String, OraxenItem> load(Path itemsFolder) {
        Map<String, OraxenItem> items = new LinkedHashMap<>();
        if (!Files.isDirectory(itemsFolder)) return items;

        try (Stream<Path> files = Files.list(itemsFolder)) {
            files.filter(p -> p.getFileName().toString().endsWith(".yml"))
                    .sorted()
                    .forEach(file -> parseFile(file, items));
        } catch (Exception e) {
            System.err.println("[Oraxen] Failed to list item files in " + itemsFolder + ": " + e.getMessage());
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    private static void parseFile(Path file, Map<String, OraxenItem> items) {
        try (InputStream in = Files.newInputStream(file)) {
            Object loaded = new Yaml().load(in);
            if (!(loaded instanceof Map<?, ?> root)) return;

            for (Map.Entry<?, ?> entry : root.entrySet()) {
                if (!(entry.getKey() instanceof String id) || !(entry.getValue() instanceof Map<?, ?> section)) {
                    continue;
                }
                OraxenItem item = parseItem(id, (Map<String, Object>) section);
                if (item != null) items.put(id, item);
            }
        } catch (Exception e) {
            System.err.println("[Oraxen] Failed to parse item file " + file.getFileName() + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static OraxenItem parseItem(String id, Map<String, Object> section) {
        String itemName = stringValue(section.get("itemname"));
        if (itemName == null) itemName = stringValue(section.get("displayname"));
        String material = stringValue(section.get("material"));
        if (material == null || material.isBlank()) {
            System.err.println("[Oraxen] Item '" + id + "' has no material; skipped");
            return null;
        }

        int customModelData = -1;
        String model = null;
        boolean generateModel = false;
        Object pack = section.get("Pack");
        if (pack instanceof Map<?, ?> packMap) {
            Map<String, Object> pm = (Map<String, Object>) packMap;
            Object cmd = pm.get("custom_model_data");
            if (cmd instanceof Number n) customModelData = n.intValue();
            model = stringValue(pm.get("model"));
            generateModel = Boolean.TRUE.equals(pm.get("generate_model"));
        }

        return OraxenItem.builder()
                .id(id)
                .itemName(itemName == null ? id : itemName)
                .material(material)
                .customModelData(customModelData)
                .model(model)
                .generateModel(generateModel)
                .nbtId("oraxen:" + id)
                .nbtType("oraxen_item")
                .enchantments(parseEnchantments(section.get("Enchantments")))
                .attributeModifiers(parseAttributeModifiers(section.get("AttributeModifiers")))
                .lore(parseLore(section.get("lore")))
                .maxDamage(firstInt(section.get("max_damage"), comp(section, "max_damage")))
                .maxStackSize(firstInt(section.get("max_stack_size"), comp(section, "max_stack_size")))
                .unbreakable(firstBool(section.get("unbreakable"), comp(section, "unbreakable")))
                .itemModel(firstString(section.get("item_model"), comp(section, "item_model")))
                .damage(firstInt(section.get("damage"), comp(section, "damage")))
                .food(parseFood(comp(section, "food")))
                .consumable(parseConsumable(comp(section, "consumable")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private static FoodComponent parseFood(Object value) {
        if (!(value instanceof Map<?, ?> m)) return null;
        Integer nutrition = intValue(m.get("nutrition"));
        if (nutrition == null) return null;
        Double saturation = numberValue(m.get("saturation"));
        boolean canAlwaysEat = Boolean.TRUE.equals(m.get("can_always_eat"));
        return new FoodComponent(nutrition, saturation != null ? saturation.floatValue() : 0f, canAlwaysEat);
    }

    @SuppressWarnings("unchecked")
    private static ConsumableComponent parseConsumable(Object value) {
        if (!(value instanceof Map<?, ?> m)) return null;
        Double seconds = numberValue(m.get("consume_seconds"));
        if (seconds == null) return null;
        String animation = stringValue(m.get("animation"));
        String sound = stringValue(m.get("sound"));
        boolean hasParticles = Boolean.TRUE.equals(m.get("has_consume_particles"));

        List<ConsumeEffectGroup> groups = null;
        Object effectsObj = m.get("on_consume_effects");
        if (effectsObj instanceof List<?> list) {
            groups = new ArrayList<>();
            for (Object o : list) {
                if (!(o instanceof Map<?, ?> group)) continue;
                String type = stringValue(group.get("type"));
                float probability = 1f;
                if (numberValue(group.get("probability")) != null) {
                    probability = numberValue(group.get("probability")).floatValue();
                }
                Map<String, PotionEffectEntry> potionEffects = null;
                Object effectMap = group.get("effects");
                if (effectMap instanceof Map<?, ?> em) {
                    potionEffects = new LinkedHashMap<>();
                    for (Map.Entry<?, ?> e : em.entrySet()) {
                        if (!(e.getKey() instanceof String effectId) || !(e.getValue() instanceof Map<?, ?> settings)) continue;
                        Integer duration = intValue(settings.get("duration"));
                        Integer amplifier = intValue(settings.get("amplifier"));
                        if (duration == null || amplifier == null) continue;
                        potionEffects.put(effectId, new PotionEffectEntry(
                                duration,
                                amplifier,
                                Boolean.TRUE.equals(settings.get("ambient")),
                                !Boolean.FALSE.equals(settings.get("show_particles")),
                                !Boolean.FALSE.equals(settings.get("show_icon"))));
                    }
                }
                if (type != null) {
                    groups.add(new ConsumeEffectGroup(type, potionEffects, probability));
                }
            }
        }

        return new ConsumableComponent(seconds.floatValue(), animation == null ? "eat" : animation,
                sound == null ? "entity.generic.eat" : sound, hasParticles, groups);
    }

    @SuppressWarnings("unchecked")
    private static Object comp(Map<String, Object> section, String key) {
        Object comp = section.get("Components");
        if (comp instanceof Map<?, ?> m) return m.get(key);
        return null;
    }

    private static Integer firstInt(Object a, Object b) {
        Integer ia = intValue(a);
        return ia != null ? ia : intValue(b);
    }

    private static Boolean firstBool(Object a, Object b) {
        Boolean ba = boolValue(a);
        return ba != null ? ba : boolValue(b);
    }

    private static String firstString(Object a, Object b) {
        String sa = stringValue(a);
        return sa != null ? sa : stringValue(b);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Integer> parseEnchantments(Object value) {
        if (!(value instanceof Map<?, ?> map)) return null;
        Map<String, Integer> out = new LinkedHashMap<>();
        for (Map.Entry<?, ?> e : map.entrySet()) {
            if (e.getKey() instanceof String k && e.getValue() instanceof Number n) {
                out.put(k, n.intValue());
            }
        }
        return out.isEmpty() ? null : out;
    }

    @SuppressWarnings("unchecked")
    private static List<AttributeModifierEntry> parseAttributeModifiers(Object value) {
        if (!(value instanceof List<?> list)) return null;
        List<AttributeModifierEntry> out = new ArrayList<>();
        for (Object o : list) {
            if (!(o instanceof Map<?, ?> m)) continue;
            String attr = stringValue(m.get("attribute"));
            Double amount = numberValue(m.get("amount"));
            Integer op = intValue(m.get("operation"));
            String slot = stringValue(m.get("slot"));
            if (attr != null && amount != null && op != null && slot != null) {
                out.add(new AttributeModifierEntry(attr, amount, op, slot));
            }
        }
        return out.isEmpty() ? null : out;
    }

    private static List<String> parseLore(Object value) {
        if (!(value instanceof List<?> list)) return null;
        List<String> out = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof String s) out.add(s);
        }
        return out.isEmpty() ? null : out;
    }

    private static String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private static Integer intValue(Object value) {
        if (value instanceof Number n) return n.intValue();
        if (value instanceof String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private static Double numberValue(Object value) {
        if (value instanceof Number n) return n.doubleValue();
        if (value instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    private static Boolean boolValue(Object value) {
        if (value instanceof Boolean b) return b;
        if (value instanceof String s) return Boolean.parseBoolean(s);
        return null;
    }
}