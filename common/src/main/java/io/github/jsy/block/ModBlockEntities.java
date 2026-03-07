package io.github.jsy.block;

import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 方块实体注册类
 */
public class ModBlockEntities {

    // 线路颜色方块实体
    public static Supplier<BlockEntityType<LineColorBlockEntity>> LINE_COLOR_BLOCK_ENTITY;

    /**
     * 注册所有方块实体
     * 由 Fabric/Forge 端的初始化代码调用
     */
    public static void registerBlockEntities(BiConsumer<String, BlockEntityType<?>> registrar) {
        registrar.accept("line_color_block_entity", LINE_COLOR_BLOCK_ENTITY.get());
    }
}
