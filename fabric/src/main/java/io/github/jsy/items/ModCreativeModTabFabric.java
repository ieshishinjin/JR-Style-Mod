// fabric/src/main/java/io/github/jrstyle/fabric/ModCreativeModeTabFabric.java
package io.github.jsy.items;

import io.github.jsy.item.ModCreativeModTab;
import io.github.jsy.util.ItemResolver;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ModCreativeModTabFabric {
    public static final CreativeModeTab FIRST_MOD_TAB;

    static {
        // 初始化common模块
        ModCreativeModTab.init();

        // 收集所有物品
        List<ItemStack> items = new ArrayList<>();
        for (String itemId : ModCreativeModTab.TAB_ITEMS) {
            ItemResolver.getItemById(itemId)
                    .ifPresent(item -> items.add(new ItemStack(item)));
        }

        // 创建图标
        ItemStack icon = ItemResolver.getItemById("jrstyle:queen")
                .map(ItemStack::new)
                .orElseGet(() -> new ItemStack(ItemResolver.getDefaultIcon()));

        // 注册Fabric物品组
        FIRST_MOD_TAB = FabricItemGroup.builder()
                .icon(() -> icon)
                .title(ModCreativeModTab.getTitle())
                .displayItems((params, output) -> {
                    for (ItemStack stack : items) {
                        output.accept(stack);
                    }
                })
                .build();
    }

    public static void register() {
        Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation("jrstyle", "firstmod_tab"),
                FIRST_MOD_TAB
        );
    }
}