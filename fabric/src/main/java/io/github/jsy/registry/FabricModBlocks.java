package io.github.jsy.registry;

import io.github.jsy.Constants;
import io.github.jsy.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class FabricModBlocks {

    public static final Block LINE_COLOR_BLOCK = ModBlocks.LINE_COLOR_BLOCK;
    public static final Block JR_STATION_SIGN_HANGING = ModBlocks.JR_STATION_SIGN_HANGING;
    public static final Block JR_STATION_SIGN_POLE = ModBlocks.JR_STATION_SIGN_POLE;

    private FabricModBlocks() {
    }

    public static void register() {
        ModBlocks.registerBlocks(FabricModBlocks::registerBlock);
    }

    private static void registerBlock(String name, Block block) {
        ResourceLocation id = new ResourceLocation(Constants.MOD_ID, name);
        Registry.register(BuiltInRegistries.BLOCK, id, block);
        Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, new Item.Properties()));
    }
}
