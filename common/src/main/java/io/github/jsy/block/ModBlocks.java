package io.github.jsy.block;

import io.github.jsy.Constants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.BiConsumer;

/**
 * 方块注册类
 */
public class ModBlocks {

    public static final Block LINE_COLOR_BLOCK = new LineColorBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()
    );
    public static final Block JR_STATION_SIGN_HANGING = new BlockJRStationSign(false);
    public static final Block JR_STATION_SIGN_POLE = new BlockJRStationSign(true);

    // 新方块
    public static final Block PLATFORM_SIGN = new BlockPlatformSign();
    public static final Block DIRECTION_SIGN = new BlockDirectionSign();

    /**
     * 注册所有方块到游戏中
     */
    public static void registerBlocks(BiConsumer<String, Block> registrar) {
        register("line_color_block", LINE_COLOR_BLOCK, registrar);
        register("jr_station_sign_hanging", JR_STATION_SIGN_HANGING, registrar);
        register("jr_station_sign_pole", JR_STATION_SIGN_POLE, registrar);
        register("platform_sign", PLATFORM_SIGN, registrar);
        register("direction_sign", DIRECTION_SIGN, registrar);
    }

    private static void register(String name, Block block, BiConsumer<String, Block> registrar) {
        Constants.LOG.debug("Registered block: {}", name);
        registrar.accept(name, block);
    }
}
