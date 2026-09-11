package io.th0rgal.oraxen.minestom;

import io.th0rgal.oraxen.core.items.OraxenItem;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomModelData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Oraxen Minestom Server - Standalone server implementation.
 */
public final class OraxenMinestomServer {

    private static final net.kyori.adventure.text.minimessage.MiniMessage MINI_MESSAGE = net.kyori.adventure.text.minimessage.MiniMessage.miniMessage();
    private static final net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer PLAIN_SERIALIZER = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText();
    private static Map<String, OraxenItem> items;

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        MinestomSchedulerAdapter.register();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instance = instanceManager.createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 64, Block.GRASS_BLOCK));

        MinecraftServer.getGlobalEventHandler().addListener(net.minestom.server.event.player.AsyncPlayerConfigurationEvent.class, event ->
                event.setSpawningInstance(instance));

        MinecraftServer.getGlobalEventHandler().addListener(net.minestom.server.event.player.PlayerSpawnEvent.class, event ->
                event.getPlayer().setRespawnPoint(new Pos(0, 65, 0)));

        items = loadItems();

        registerCommands();

        int port = Integer.getInteger("minestom.port", 25565);
        server.start("0.0.0.0", port);
        System.out.println("Oraxen-Minestom started on port " + port);
    }

    private static Map<String, OraxenItem> loadItems() {
        Path itemsFolder = Path.of("oraxen", "items");
        try {
            if (!Files.isDirectory(itemsFolder)) {
                Files.createDirectories(itemsFolder);
                System.out.println("[Oraxen] Created empty items folder at " + itemsFolder.toAbsolutePath()
                        + " - add item .yml files and restart.");
            }
        } catch (Exception e) {
            System.err.println("[Oraxen] Failed to create items folder: " + e.getMessage());
        }
        Map<String, OraxenItem> loaded = MinestomItemLoader.load(itemsFolder);
        System.out.println("[Oraxen] Loaded " + loaded.size() + " item(s)");
        return loaded;
    }

    private static void registerCommands() {
        net.minestom.server.command.CommandManager commandManager = MinecraftServer.getCommandManager();
        net.minestom.server.command.builder.Command oraxenCmd = new net.minestom.server.command.builder.Command("oraxen");
        net.minestom.server.command.builder.Command giveCmd = new net.minestom.server.command.builder.Command("give");

        giveCmd.setDefaultExecutor((sender, context) ->
                sender.sendMessage(net.kyori.adventure.text.Component.text("Usage: /oraxen give <id> [amount]")));

        giveCmd.addSyntax((sender, context) ->
                        giveItem(sender, context.get("id"), 1),
                net.minestom.server.command.builder.arguments.ArgumentType.Word("id"));

        giveCmd.addSyntax((sender, context) ->
                        giveItem(sender, context.get("id"), context.get("amount")),
                net.minestom.server.command.builder.arguments.ArgumentType.Word("id"), net.minestom.server.command.builder.arguments.ArgumentType.Integer("amount"));

        oraxenCmd.addSubcommand(giveCmd);
        commandManager.register(oraxenCmd);
        System.out.println("[Oraxen] Commands registered: /oraxen give");
    }

    private static void giveItem(net.minestom.server.command.CommandSender sender, String id, int amount) {
        if (!(sender instanceof net.minestom.server.entity.Player player)) {
            sender.sendMessage(net.kyori.adventure.text.Component.text("<red>Only players can use this command"));
            return;
        }

        OraxenItem item = items.get(id);
        if (item == null) {
            sender.sendMessage(net.kyori.adventure.text.Component.text("<red>Unknown Oraxen item: " + id));
            return;
        }

        Material material = MinestomMaterialResolver.resolve(item.material());
        if (material == null) {
            sender.sendMessage(net.kyori.adventure.text.Component.text("<red>Unmapped material for item '" + id + "': " + item.material()));
            return;
        }

        // Parse item name with MiniMessage (supports gradients, colors, etc.)
        net.kyori.adventure.text.Component itemName = net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(item.itemName());

        net.minestom.server.item.ItemStack.Builder builder = net.minestom.server.item.ItemStack.builder(material).amount(Math.max(1, amount));
        builder.customName(itemName);

        // Apply lore (each line parsed with MiniMessage)
        if (item.lore() != null && !item.lore().isEmpty()) {
            builder.lore(item.lore().stream()
                    .map(line -> net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(line))
                    .toList());
        }

        // Apply max stack size
        if (item.maxStackSize() != null) {
            builder.set(net.minestom.server.component.DataComponents.MAX_STACK_SIZE, item.maxStackSize());
        }

        // Apply unbreakable
        if (Boolean.TRUE.equals(item.unbreakable())) {
            builder.set(net.minestom.server.component.DataComponents.UNBREAKABLE, net.minestom.server.utils.Unit.INSTANCE);
        }

        // Apply damage / max damage
        if (item.damage() != null) {
            builder.set(net.minestom.server.component.DataComponents.DAMAGE, item.damage());
        }
        if (item.maxDamage() != null) {
            builder.set(net.minestom.server.component.DataComponents.MAX_DAMAGE, item.maxDamage());
        }

        // Apply custom model data if present
        if (item.customModelData() >= 0) {
            builder.set(net.minestom.server.component.DataComponents.CUSTOM_MODEL_DATA,
                    new net.minestom.server.item.component.CustomModelData(
                            List.of((float) item.customModelData()), List.of(), List.of(), List.of()));
        }

        // Apply enchantments, attribute modifiers, food
        MinestomItemApplier.apply(builder, item);

        // Add Oraxen NBT marker for round-trip identification
        net.kyori.adventure.nbt.CompoundBinaryTag nbt = net.kyori.adventure.nbt.CompoundBinaryTag.builder()
                .putString("oraxen_id", item.nbtId())
                .putString("oraxen_type", item.nbtType())
                .build();
        builder.set(net.minestom.server.component.DataComponents.CUSTOM_DATA,
                new net.minestom.server.item.component.CustomData(nbt));

        player.getInventory().addItemStack(builder.build());
        sender.sendMessage(net.kyori.adventure.text.Component.text("<green>Given " + amount + "x " + id));
    }
}