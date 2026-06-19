package io.github.jsy.registry;

import io.github.jsy.Constants;
import io.github.jsy.block.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class FabricModBlockEntities {

    public static BlockEntityType<LineColorBlockEntity> LINE_COLOR_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityJRStationSign> JR_STATION_SIGN_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityPlatformSign> PLATFORM_SIGN_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityDirectionSign> DIRECTION_SIGN_BLOCK_ENTITY;

    private FabricModBlockEntities() {}

    public static void register() {
        LINE_COLOR_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "line_color_block_entity"),
                BlockEntityType.Builder.of(LineColorBlockEntity::new, ModBlocks.LINE_COLOR_BLOCK).build(null)
        );
        JR_STATION_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "jr_station_sign_block_entity"),
                BlockEntityType.Builder.of(BlockEntityJRStationSign::new,
                        ModBlocks.JR_STATION_SIGN_HANGING, ModBlocks.JR_STATION_SIGN_POLE).build(null)
        );
        PLATFORM_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "platform_sign_block_entity"),
                BlockEntityType.Builder.of(BlockEntityPlatformSign::new, ModBlocks.PLATFORM_SIGN).build(null)
        );
        DIRECTION_SIGN_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "direction_sign_block_entity"),
                BlockEntityType.Builder.of(BlockEntityDirectionSign::new, ModBlocks.DIRECTION_SIGN).build(null)
        );

        ModBlockEntities.LINE_COLOR_BLOCK_ENTITY = LINE_COLOR_BLOCK_ENTITY;
        ModBlockEntities.JR_STATION_SIGN_BLOCK_ENTITY = JR_STATION_SIGN_BLOCK_ENTITY;
        ModBlockEntities.PLATFORM_SIGN_BLOCK_ENTITY = PLATFORM_SIGN_BLOCK_ENTITY;
        ModBlockEntities.DIRECTION_SIGN_BLOCK_ENTITY = DIRECTION_SIGN_BLOCK_ENTITY;
    }
}
