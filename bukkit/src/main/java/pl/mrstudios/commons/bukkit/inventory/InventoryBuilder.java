package pl.mrstudios.commons.bukkit.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.commons.bukkit.item.ItemBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class InventoryBuilder implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, BiConsumer<Player, InventoryClickEvent>> handlers;

    private InventoryService inventoryService;

    private @Nullable Component title;
    private @Nullable InventoryType inventoryType;
    private final @NotNull Integer size;

    public InventoryBuilder(@NotNull Integer size) {
        this.size = size;
        this.handlers = new HashMap<>();
        this.inventory = Bukkit.createInventory(this, size);
    }

    public InventoryBuilder(@NotNull Component title) {
        this.title = title;
        this.handlers = new HashMap<>();
        this.size = DEFAULT_INVENTORY_SIZE;
        this.inventory = Bukkit.createInventory(this, DEFAULT_INVENTORY_SIZE, this.title);
    }

    public InventoryBuilder(@NotNull InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        this.handlers = new HashMap<>();
        this.size = this.inventoryType.getDefaultSize();
        this.inventory = Bukkit.createInventory(this, this.inventoryType);
    }

    public InventoryBuilder(@NotNull Component title, @NotNull InventoryType inventoryType) {
        this.title = title;
        this.handlers = new HashMap<>();
        this.inventoryType = inventoryType;
        this.size = this.inventoryType.getDefaultSize();
        this.inventory = Bukkit.createInventory(this, this.inventoryType, title);
    }

    public InventoryBuilder(@NotNull Component title, @NotNull Integer size) {
        this.size = size;
        this.title = title;
        this.handlers = new HashMap<>();
        this.inventory = Bukkit.createInventory(this, this.size, title);
    }

    /* Getter */
    public @Nullable Component title() {
        return this.title;
    }

    public @Nullable InventoryType inventoryType() {
        return this.inventoryType;
    }

    public @NotNull Integer size() {
        return this.size;
    }

    /* Setter */
    public InventoryBuilder service(@NotNull InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        return this;
    }

    public InventoryBuilder fill(@NotNull ItemBuilder itemBuilder) {

        for (int i = 0; i < this.size; i++)
            this.inventory.setItem(i, itemBuilder.build());

        for (int i = 0; i < this.size; i++)
            this.handlers.put(i, DEFAULT_CONSUMER);

        return this;

    }

    public InventoryBuilder fill(@NotNull ItemBuilder itemBuilder, @NotNull BiConsumer<Player, InventoryClickEvent> consumer) {

        for (int i = 0; i < this.size; i++)
            this.inventory.setItem(i, itemBuilder.build());

        for (int i = 0; i < this.size; i++)
            this.handlers.put(i, consumer);

        return this;

    }

    public InventoryBuilder item(@NotNull Integer slot, @NotNull ItemBuilder itemBuilder) {
        this.inventory.setItem(slot, itemBuilder.build());
        return this;
    }

    public InventoryBuilder item(@NotNull Integer slot, @NotNull ItemBuilder itemBuilder, @NotNull BiConsumer<Player, InventoryClickEvent> consumer) {
        this.inventory.setItem(slot, itemBuilder.build());
        this.handlers.put(slot, consumer);
        return this;
    }

    public InventoryBuilder register() {

        if (this.title == null)
            return this;

        if (this.inventoryService == null)
            throw new NullPointerException("You must provide inventoryService if you want to use handlers.");

        this.inventoryService.register(
                this, this.handlers.keySet()
                        .stream()
                        .map((i) -> new InventoryHandler(i, this.handlers.get(i)))
                        .toList()
        );

        return this;

    }

    public void display(@NotNull Player player) {
        player.openInventory(this.inventory);
    }

    protected static final int DEFAULT_INVENTORY_SIZE = 9;
    protected static final BiConsumer<Player, InventoryClickEvent> DEFAULT_CONSUMER = (player, event) -> {
        event.setCancelled(true);
    };

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

}
