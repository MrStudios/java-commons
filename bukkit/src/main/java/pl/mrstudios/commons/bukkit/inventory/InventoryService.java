package pl.mrstudios.commons.bukkit.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InventoryService implements Listener {

    public InventoryService(@NotNull Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void register(@NotNull InventoryHolder holder, @NotNull Collection<InventoryHandler> collection) {
        this.handlers.put(holder, collection);
    }

    @Deprecated
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player))
            return;

        if (event.getClickedInventory() == null)
            return;

        if (event.getClickedInventory().getHolder() == null)
            return;

        Collection<InventoryHandler> collection = this.handlers.get(event.getClickedInventory().getHolder());
        if (collection == null)
            return;

        collection.stream()
                .filter((handler) -> handler.slot() == event.getSlot())
                .findFirst()
                .ifPresent((handler) -> handler.consumer().accept(player, event));

    }

    protected final Map<InventoryHolder, Collection<InventoryHandler>> handlers = new HashMap<>();

}
