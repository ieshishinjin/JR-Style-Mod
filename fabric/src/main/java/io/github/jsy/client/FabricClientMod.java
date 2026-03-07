package io.github.jsy.client;

import io.github.jsy.Constants;
import io.github.jsy.client.render.LineColorBlockEntityRenderer;
import io.github.jsy.registry.FabricModBlockEntities;
import io.github.jsy.registry.FabricModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Fabric 客户端初始化
 */
public class FabricClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Constants.LOG.info("Initializing JR Style Mod client for Fabric");

        // 注册方块实体渲染器
        BlockEntityRendererRegistry.register(
                FabricModBlockEntities.LINE_COLOR_BLOCK_ENTITY,
                LineColorBlockEntityRenderer::new
        );

        // 注册方块颜色提供器（用于 tintindex）
        ColorProviderRegistry.BLOCK.register(new BlockColor() {
            @Override
            public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
                if (level == null || pos == null) {
                    return 0xFFFFFFFF;
                }

                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof io.github.jsy.block.LineColorBlockEntity lineColorBE) {
                    // tintIndex 0 用于线路颜色
                    if (tintIndex == 0) {
                        return lineColorBE.getLineColor();
                    }
                }

                return 0xFFFFFFFF;
            }
        }, FabricModBlocks.LINE_COLOR_BLOCK);
    }
}
