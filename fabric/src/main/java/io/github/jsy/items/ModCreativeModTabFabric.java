package io.github.jsy.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import static io.github.jsy.items.ModitemsFabric.*;
import static io.github.jsy.block.ModblockFabric.*;

public class ModCreativeModTabFabric {
    public static final CreativeModeTab JR_STYLE_TAB =
            FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup.jsy.main"))
                    .icon(() -> King.getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(King.getDefaultInstance());
                        output.accept(Queen.getDefaultInstance());
                        output.accept(Prince.getDefaultInstance());
                        output.accept(Kingdom.asItem().getDefaultInstance());
                    })
                    .build();
    public static void register() {
        // 注册物品栏
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation("jsy", "main"),
                JR_STYLE_TAB
        );
    }
}