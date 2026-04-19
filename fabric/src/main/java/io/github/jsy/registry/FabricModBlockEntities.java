package io.github.jsy.registry;

import io.github.jsy.Constants;
import io.github.jsy.block.BlockEntityJRStationSign;
import io.github.jsy.block.LineColorBlockEntity;
import io.github.jsy.block.ModBlockEntities;
import io.github.jsy.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class FabricModBlockEntities {

    public static BlockEntityType<LineColorBlockEntity> LINE_COLOR_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityJRStationSign> JR_STATION_SIGN_BLOCK_ENTITY;

    private FabricModBlockEntities() {
    }

    public static void register() {
        LINE_COLOR_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "line_color_block_entity"),
                BlockEntityType.Builder.of(LineColorBlockEntity::new, ModBlocks.LINE_COLOR_BLOCK).build(null)
        );
        JR_STATION_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "jr_station_sign_block_entity"),
                BlockEntityType.Builder.of(BlockEntityJRStationSign::new, ModBlocks.JR_STATION_SIGN_HANGING, ModBlocks.JR_STATION_SIGN_POLE).build(null)
        );
        ModBlockEntities.LINE_COLOR_BLOCK_ENTITY = LINE_COLOR_BLOCK_ENTITY;
        ModBlockEntities.JR_STATION_SIGN_BLOCK_ENTITY = JR_STATION_SIGN_BLOCK_ENTITY;
    }
}
