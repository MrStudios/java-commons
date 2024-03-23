# Java Commons (Bukkit)
That library is for easiest inventory and item building for Bukkit platform.

## Usage
To use this library, you need to add following to your project build file.

Maven:
```xml
<dependency>
    <groupId>pl.mrstudios.commons</groupId>
    <artifactId>commons-bukkit</artifactId>
    <version>1.1.0</version>
</dependency>
```

Gradle: (Groovy)
```groovy
implementation "pl.mrstudios.commons:commons-bukkit:1.1.0"
```

Gradle (Kotlin)
```kotlin
implementation("pl.mrstudios.commons:commons-bukkit:1.1.0")
```

## Example
```java
package pl.mrstudios.example;

import org.bukkit.plugin.java.JavaPlugin;
import pl.mrstudios.commons.bukkit.inventory.InventoryService;
import pl.mrstudios.example.command.ExampleCommand;

public class Example extends JavaPlugin {

    private InventoryService inventoryService;

    @Override
    public void onEnable() {

        this.inventoryService = new InventoryService(this);
        this.getCommand("server").setExecutor(new ExampleCommand(this.inventoryService));

    }

}
```

```java
package pl.mrstudios.example.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.bukkit.inventory.InventoryBuilder;
import pl.mrstudios.commons.bukkit.inventory.InventoryService;
import pl.mrstudios.commons.bukkit.item.ItemBuilder;

public class ExampleCommand implements CommandExecutor {

    private final InventoryService inventoryService;
    protected final InventoryBuilder inventoryBuilder;

    public Example(@NotNull InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.inventoryBuilder = new InventoryBuilder(Component.text("Server Selector"), 27)
                .item(13, new ItemBuilder(Material.DIAMOND)
                                .name(Component.text("Survival"))
                                .lore(
                                        Component.empty(),
                                        Component.text("Currently: " + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers()),
                                        Component.empty(),
                                        Component.text("Click to join!")
                                ),
                        (player, event) -> player.sendMessage("This server is currently disabled, please try again later.")
                )
                .service(this.inventoryService)
                .register();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player player))
            return false;

        this.inventoryBuilder.display(player);

        return false;

    }

}
```


