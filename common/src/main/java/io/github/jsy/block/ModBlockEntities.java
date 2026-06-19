package io.github.jsy.block;

import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

/**
 * 方块实体注册类
 */
public class ModBlockEntities {

    public static BlockEntityType<LineColorBlockEntity> LINE_COLOR_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityJRStationSign> JR_STATION_SIGN_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityPlatformSign> PLATFORM_SIGN_BLOCK_ENTITY;
    public static BlockEntityType<BlockEntityDirectionSign> DIRECTION_SIGN_BLOCK_ENTITY;

    /**
     * 注册所有方块实体
     */
    public static void registerBlockEntities(BiConsumer<String, BlockEntityType<?>> registrar) {
        registrar.accept("line_color_block_entity", LINE_COLOR_BLOCK_ENTITY);
        registrar.accept("jr_station_sign_block_entity", JR_STATION_SIGN_BLOCK_ENTITY);
        registrar.accept("platform_sign_block_entity", PLATFORM_SIGN_BLOCK_ENTITY);
        registrar.accept("direction_sign_block_entity", DIRECTION_SIGN_BLOCK_ENTITY);
    }
}
