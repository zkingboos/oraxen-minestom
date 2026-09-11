package io.th0rgal.oraxen.core.items;

import java.util.List;
import java.util.Map;

/**
 * Neutral Oraxen item model - core fields only.
 */
public record OraxenItem(
        String id,
        String itemName,
        String material,
        int customModelData,
        String model,
        boolean generateModel,
        String nbtId,
        String nbtType,
        Map<String, Integer> enchantments,
        List<AttributeModifierEntry> attributeModifiers,
        Integer maxDamage,
        Integer maxStackSize,
        Boolean unbreakable,
        List<String> lore,
        String itemModel,
        Integer damage,
        FoodComponent food,
        ConsumableComponent consumable,
        PotionContentsComponent potionContents,
        Integer dyedColor,
        String jukeboxSong,
        Integer maxDamageOverride,
        Integer repairCost,
        ToolComponent tool,
        Boolean enchantmentGlintOverride
) {
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id, itemName, material, model, nbtId, nbtType, itemModel, jukeboxSong;
        private int customModelData = -1;
        private boolean generateModel;
        private Map<String, Integer> enchantments;
        private List<AttributeModifierEntry> attributeModifiers;
        private Integer maxDamage, maxStackSize, damage, maxDamageOverride, repairCost, dyedColor;
        private Boolean unbreakable, enchantmentGlintOverride;
        private List<String> lore;
        private FoodComponent food;
        private ConsumableComponent consumable;
        private PotionContentsComponent potionContents;
        private ToolComponent tool;

        public Builder id(String v) { id = v; return this; }
        public Builder itemName(String v) { itemName = v; return this; }
        public Builder material(String v) { material = v; return this; }
        public Builder customModelData(int v) { customModelData = v; return this; }
        public Builder model(String v) { model = v; return this; }
        public Builder generateModel(boolean v) { generateModel = v; return this; }
        public Builder nbtId(String v) { nbtId = v; return this; }
        public Builder nbtType(String v) { nbtType = v; return this; }
        public Builder enchantments(Map<String, Integer> v) { enchantments = v; return this; }
        public Builder attributeModifiers(List<AttributeModifierEntry> v) { attributeModifiers = v; return this; }
        public Builder maxDamage(Integer v) { maxDamage = v; return this; }
        public Builder maxStackSize(Integer v) { maxStackSize = v; return this; }
        public Builder unbreakable(Boolean v) { unbreakable = v; return this; }
        public Builder lore(List<String> v) { lore = v; return this; }
        public Builder itemModel(String v) { itemModel = v; return this; }
        public Builder damage(Integer v) { damage = v; return this; }
        public Builder food(FoodComponent v) { food = v; return this; }
        public Builder consumable(ConsumableComponent v) { consumable = v; return this; }
        public Builder potionContents(PotionContentsComponent v) { potionContents = v; return this; }
        public Builder dyedColor(Integer v) { dyedColor = v; return this; }
        public Builder jukeboxSong(String v) { jukeboxSong = v; return this; }
        public Builder maxDamageOverride(Integer v) { maxDamageOverride = v; return this; }
        public Builder repairCost(Integer v) { repairCost = v; return this; }
        public Builder tool(ToolComponent v) { tool = v; return this; }
        public Builder enchantmentGlintOverride(Boolean v) { enchantmentGlintOverride = v; return this; }

        public OraxenItem build() {
            return new OraxenItem(id, itemName, material, customModelData, model, generateModel, nbtId, nbtType,
                    enchantments, attributeModifiers, maxDamage, maxStackSize, unbreakable, lore, itemModel, damage,
                    food, consumable, potionContents, dyedColor, jukeboxSong, maxDamageOverride, repairCost, tool, enchantmentGlintOverride);
        }
    }
}