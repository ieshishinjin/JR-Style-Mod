// common/src/main/java/io/github/jrstyle/item/ModCreativeModeTab.java
package io.github.jsy.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class ModCreativeModTab {
    // 创造标签页的注册键（所有加载器共享）
    public static final ResourceKey<CreativeModeTab> FIRST_MOD_TAB =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    new ResourceLocation("jsy", "firstmod_tab")
            );

    // 标签页标题翻译键
    public static final String TAB_TITLE_KEY = "itemGroup.first_tab";

    // 标签页图标物品ID
    public static final String TAB_ICON_ITEM = "queen"; // 使用queen作为图标

    // 标签页包含的物品ID列表（按顺序）
    public static final String[] TAB_ITEMS = {
            "jrstyle:king",
            "jrstyle:queen",
            "jrstyle:prince",
            "minecraft:apple",
            "minecraft:diamond"
    };

    /**
     * 获取标签页标题组件
     */
    public static Component getTitle() {
        return Component.translatable(TAB_TITLE_KEY);
    }

    /**
     * 初始化方法 - 可以在所有加载器中调用
     */
    public static void init() {
        // 这里可以添加标签页的通用初始化逻辑
    }
}