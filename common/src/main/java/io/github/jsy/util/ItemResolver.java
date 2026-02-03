// common/src/main/java/io/github/jrstyle/util/ItemResolver.java
package io.github.jsy.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable; // Minecraft 自带了这个

import java.util.Optional;

/**
 * 物品解析工具类 - 用于根据字符串ID查找物品
 * 这个类可以在所有加载器（Forge/Fabric/NeoForge）的Common模块中使用
 */
public class ItemResolver {

    /**
     * 根据字符串ID获取物品
     * 支持格式:
     * 1. "modid:itemname" - 完整格式
     * 2. "itemname" - 默认modid为"minecraft"
     * 3. "#tag:modid/tag_name" - 物品标签（可选功能）
     *
     * @param itemId 物品ID字符串
     * @return 物品的Optional，找不到时返回Optional.empty()
     */
    public static Optional<Item> getItemById(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            return Optional.empty();
        }

        try {
            ResourceLocation location;

            if (itemId.contains(":")) {
                // 完整格式: "modid:itemname"
                location = ResourceLocation.tryParse(itemId);
            } else {
                // 简写格式: "itemname" -> "minecraft:itemname"
                location = new ResourceLocation("minecraft", itemId);
            }

            if (location != null && BuiltInRegistries.ITEM.containsKey(location)) {
                Item item = BuiltInRegistries.ITEM.get(location);
                return Optional.ofNullable(item);
            }
        } catch (Exception e) {
            // 无效的ResourceLocation或注册表访问错误
            System.err.println("Failed to resolve item: " + itemId + " - " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * 获取物品，如果找不到则返回默认物品
     *
     * @param itemId 物品ID字符串
     * @param defaultItem 默认物品
     * @return 找到的物品或默认物品
     */
    @Nullable
    public static Item getItemByIdOrElse(String itemId, @Nullable Item defaultItem) {
        return getItemById(itemId).orElse(defaultItem);
    }

    /**
     * 获取物品，如果找不到则返回钻石
     *
     * @param itemId 物品ID字符串
     * @return 找到的物品或钻石
     */
    public static Item getItemByIdOrDiamond(String itemId) {
        return getItemById(itemId).orElse(getDefaultIcon());
    }

    /**
     * 获取默认图标物品（当指定物品找不到时）
     *
     * @return 默认物品（钻石）
     */
    public static Item getDefaultIcon() {
        return Items.DIAMOND;
    }

    /**
     * 检查物品是否存在
     *
     * @param itemId 物品ID字符串
     * @return 是否存在
     */
    public static boolean itemExists(String itemId) {
        return getItemById(itemId).isPresent();
    }

    /**
     * 从物品创建ItemStack
     *
     * @param itemId 物品ID字符串
     * @param count 数量
     * @return ItemStack的Optional
     */
    public static Optional<net.minecraft.world.item.ItemStack> getItemStackById(String itemId, int count) {
        return getItemById(itemId).map(item -> new net.minecraft.world.item.ItemStack(item, count));
    }

    /**
     * 从物品创建单个ItemStack
     *
     * @param itemId 物品ID字符串
     * @return ItemStack的Optional
     */
    public static Optional<net.minecraft.world.item.ItemStack> getItemStackById(String itemId) {
        return getItemStackById(itemId, 1);
    }

    /**
     * 解析多个物品ID
     *
     * @param itemIds 物品ID数组
     * @return 物品数组
     */
    public static Item[] getItemsByIds(String... itemIds) {
        Item[] items = new Item[itemIds.length];
        for (int i = 0; i < itemIds.length; i++) {
            items[i] = getItemByIdOrDiamond(itemIds[i]);
        }
        return items;
    }
}