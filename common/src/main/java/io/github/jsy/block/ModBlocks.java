package io.github.jsy.block;

import io.github.jsy.Constants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 方块注册类
 */
public class ModBlocks {

    // 线路颜色方块
    public static final Supplier<Block> LINE_COLOR_BLOCK = register(
            "line_color_block",
            () -> new LineColorBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(2.0f, 6.0f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
            )
    );

    private static Supplier<Block> register(String name, Supplier<Block> block) {
        return () -> {
            Block registeredBlock = block.get();
            Constants.LOG.debug("Registered block: {}", name);
            return registeredBlock;
        };
    }

    /**
     * 注册所有方块到游戏中
     * 由 Fabric/Forge 端的初始化代码调用
     */
    public static void registerBlocks(BiConsumer<String, Block> registrar) {
        registrar.accept("line_color_block", LINE_COLOR_BLOCK.get());
    }
}
