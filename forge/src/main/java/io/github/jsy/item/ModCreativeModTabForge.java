// forge/src/main/java/io/github/jrstyle/forge/ModCreativeModeTabForge.java
package io.github.jsy.item;

import io.github.jsy.util.ItemResolver;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabForge {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "jsy");

    public static final RegistryObject<CreativeModeTab> FIRST_MOD_TAB =
            CREATIVE_MODE_TABS.register("firstmod_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> {
                                // 使用queen作为图标
                                ItemStack icon = ItemResolver.getItemById("jsy:queen")
                                        .map(ItemStack::new)
                                        .orElseGet(() -> new ItemStack(ItemResolver.getDefaultIcon()));
                                return icon;
                            })
                            .title(ModCreativeModTab.getTitle())
                            .displayItems((params, output) -> {
                                // 添加所有物品
                                for (String itemId : ModCreativeModTab.TAB_ITEMS) {
                                    ItemResolver.getItemById(itemId)
                                            .ifPresent(item -> output.accept(new ItemStack(item)));
                                }
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        // 初始化common模块
        ModCreativeModTab.init();
        // 注册标签页
        CREATIVE_MODE_TABS.register(eventBus);
    }
}