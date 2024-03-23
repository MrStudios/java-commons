package pl.mrstudios.commons.bukkit.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.*;

import static java.util.UUID.randomUUID;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer.get;

public class ItemBuilder {

    /* Item Required Data */
    private final @NotNull Material material;
    private final @NotNull Integer amount;

    /* Item Optional Data */
    private @Nullable Component name;
    private @Nullable List<Component> lore;
    private @Nullable Collection<ItemFlag> itemFlags;
    private @Nullable Map<Enchantment, Integer> enchantments;
    private @Nullable Map<Attribute, AttributeModifier> attributes;
    private @Nullable Integer customModelData;
    private @Nullable Boolean unbreakable;
    private @Nullable GameProfile gameProfile;

    public ItemBuilder(
            @NotNull Material material
    ) {
        this.material = material;
        this.amount = 1;
    }

    public ItemBuilder(
            @NotNull Material material,
            @NotNull Integer amount
    ) {
        this.material = material;
        this.amount = amount;
    }

    public ItemBuilder name(
            @NotNull Component name
    ) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(
            @NotNull List<Component> lore
    ) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder lore(
            @NotNull Component... lore
    ) {
        this.lore = List.of(lore);
        return this;
    }

    public ItemBuilder itemFlags(
            @NotNull ItemFlag... itemFlags
    ) {
        this.itemFlags = List.of(itemFlags);
        return this;
    }

    public ItemBuilder itemFlags(
            @NotNull Collection<ItemFlag> itemFlags
    ) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder attribute(
            @NotNull Attribute attribute,
            @NotNull AttributeModifier attributeModifier
    ) {

        if (this.attributes == null)
            this.attributes = new EnumMap<>(Attribute.class);

        this.attributes.put(attribute, attributeModifier);
        return this;

    }

    public ItemBuilder attributes(
            @NotNull Map<Attribute, AttributeModifier> attributes
    ) {
        this.attributes = attributes;
        return this;
    }

    public ItemBuilder enchantments(
            @NotNull Enchantment enchantment,
            @NotNull Integer level
    ) {

        if (this.enchantments == null)
            this.enchantments = new HashMap<>();

        this.enchantments.put(enchantment, level);
        return this;

    }

    public ItemBuilder enchantments(
            @NotNull Map<Enchantment, Integer> enchantments
    ) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder customModelData(
            @NotNull Integer customModelData
    ) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder unbreakable(
            @NotNull Boolean unbreakable
    ) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder texture(
            @NotNull String texture
    ) {
        this.gameProfile = new GameProfile(randomUUID(), "Player");
        this.gameProfile.getProperties().put("textures", new Property("textures", texture));
        return this;
    }

    public ItemBuilder skullOwner(
            @NotNull String player
    ) {
        this.gameProfile = new GameProfile(null, player);
        return this;
    }

    public ItemBuilder skullOwner(
            @NotNull Player player
    ) {
        this.gameProfile = new GameProfile(null, player.getName());
        return this;
    }

    @SuppressWarnings("all")
    public ItemStack build() {

        ItemStack itemStack = new ItemStack(this.material, this.amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.name != null)
            itemMeta.setDisplayNameComponent(bungeeComponentSerializer.serialize(this.name.decoration(ITALIC, false)));

        if (this.lore != null)
            itemMeta.setLoreComponents(
                    this.lore.stream()
                            .map((component) -> component.decoration(ITALIC, false))
                            .map(bungeeComponentSerializer::serialize)
                            .toList()
            );

        if (this.itemFlags != null)
            itemMeta.addItemFlags(this.itemFlags.toArray(ItemFlag[]::new));

        if (this.attributes != null)
            this.attributes.forEach(itemMeta::addAttributeModifier);

        if (this.enchantments != null)
            this.enchantments.forEach(
                    (enchantment, level) -> itemMeta.addEnchant(enchantment, level, true)
            );

        if (this.customModelData != null)
            itemMeta.setCustomModelData(this.customModelData);

        if (this.unbreakable != null)
            itemMeta.setUnbreakable(this.unbreakable);

        if (this.gameProfile != null)
            if (itemMeta instanceof SkullMeta skullMeta)
                try {

                    if (this.gameProfile.getProperties().isEmpty())
                        skullMeta.setOwner(this.gameProfile.getName());

                    else {

                        Method method = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                        if (!method.isAccessible())
                            method.setAccessible(true);

                        method.invoke(skullMeta, this.gameProfile);

                    }

                } catch (@NotNull Exception ignored) {}

        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }

    protected static final BungeeComponentSerializer bungeeComponentSerializer = get();

}
