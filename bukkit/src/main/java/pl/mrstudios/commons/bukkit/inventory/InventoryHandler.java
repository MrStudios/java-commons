package pl.mrstudios.commons.bukkit.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public record InventoryHandler(
        @NotNull Integer slot,
        @NotNull BiConsumer<Player, InventoryClickEvent> consumer
) {}
