// neoforge/src/main/java/io/github/jrstyle/neoforge/ModCreativeModeTabNeoForge.java
package io.github.jrstyle.items;

import io.github.jrstyle.item.ModCreativeModTab;
import io.github.jrstyle.util.ItemResolver;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModTabNeoforge {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "jrstyle");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FIRST_MOD_TAB =
            CREATIVE_MODE_TABS.register("firstmod_tab",
                    () -> CreativeModeTab.builder()
                            .icon(() -> {
                                ItemStack icon = ItemResolver.getItemById("jrstyle:queen")
                                        .map(ItemStack::new)
                                        .orElseGet(() -> new ItemStack(ItemResolver.getDefaultIcon()));
                                return icon;
                            })
                            .title(ModCreativeModTab.getTitle())
                            .displayItems((params, output) -> {
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