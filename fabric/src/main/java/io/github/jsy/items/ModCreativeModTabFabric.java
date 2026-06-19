package io.github.jsy.items;

import io.github.jsy.Constants;
import io.github.jsy.registry.FabricModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModCreativeModTabFabric {
    public static final CreativeModeTab JR_STYLE_TAB =
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.jsy.main"))
                    .icon(() -> new ItemStack(Items.MINECART))
                    .displayItems((params, output) -> {
                        output.accept(FabricModBlocks.LINE_COLOR_BLOCK.asItem());
                        output.accept(FabricModBlocks.JR_STATION_SIGN_HANGING.asItem());
                        output.accept(FabricModBlocks.JR_STATION_SIGN_POLE.asItem());
                        output.accept(FabricModBlocks.PLATFORM_SIGN.asItem());
                        output.accept(FabricModBlocks.DIRECTION_SIGN.asItem());
                    })
                    .build();

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation(Constants.MOD_ID, "main"),
                JR_STYLE_TAB
        );
    }
}
