package io.github.jsy.item;

import io.github.jsy.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabForge {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "jsy");

    public static final RegistryObject<CreativeModeTab> JR_STYLE_TAB =
            TABS.register("main", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.jsy.main"))
                            .icon(() -> new ItemStack(Items.MINECART))
                            .displayItems((params, output) -> {
                                output.accept(ModBlocks.LINE_COLOR_BLOCK.asItem());
                                output.accept(ModBlocks.JR_STATION_SIGN_HANGING.asItem());
                                output.accept(ModBlocks.JR_STATION_SIGN_POLE.asItem());
                            })
                            .build()
            );

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
