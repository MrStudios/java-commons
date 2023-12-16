package pl.mrstudios.commons.bukkit.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    public ItemBuilder(@NotNull Material material) {
        this.material = material;
        this.amount = 1;
    }

    public ItemBuilder(@NotNull Material material, @NotNull Integer amount) {
        this.material = material;
        this.amount = amount;
    }

    public ItemBuilder name(@NotNull Component name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(@NotNull List<Component> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder lore(@NotNull Component... lore) {
        this.lore = List.of(lore);
        return this;
    }

    public ItemBuilder itemFlags(@NotNull ItemFlag... itemFlags) {
        this.itemFlags = List.of(itemFlags);
        return this;
    }

    public ItemBuilder itemFlags(@NotNull Collection<ItemFlag> itemFlags) {
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder attribute(@NotNull Attribute attribute, @NotNull AttributeModifier attributeModifier) {

        if (this.attributes == null)
            this.attributes = new EnumMap<>(Attribute.class);

        this.attributes.put(attribute, attributeModifier);
        return this;

    }

    public ItemBuilder attributes(@NotNull Map<Attribute, AttributeModifier> attributes) {
        this.attributes = attributes;
        return this;
    }

    public ItemBuilder enchantments(@NotNull Enchantment enchantment, @NotNull Integer level) {

        if (this.enchantments == null)
            this.enchantments = new HashMap<>();

        this.enchantments.put(enchantment, level);
        return this;

    }

    public ItemBuilder enchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder customModelData(@NotNull Integer customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder unbreakable(@NotNull Boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    @SuppressWarnings("all")
    public ItemStack build() {

        ItemStack itemStack = new ItemStack(this.material, this.amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.name != null)
            itemMeta.setDisplayNameComponent(bungeeComponentSerializer.serialize(this.name.decoration(TextDecoration.ITALIC, false)));


        if (this.lore != null)
            itemMeta.setLoreComponents(
                    this.lore.stream()
                            .map((component) -> component.decoration(TextDecoration.ITALIC, false))
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

        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }

    protected static final BungeeComponentSerializer bungeeComponentSerializer = BungeeComponentSerializer.get();

}
