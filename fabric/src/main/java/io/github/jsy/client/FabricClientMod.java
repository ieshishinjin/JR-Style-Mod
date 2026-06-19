package io.github.jsy.client;

import io.github.jsy.Constants;
import io.github.jsy.block.LineColorBlockEntity;
import io.github.jsy.block.ModBlocks;
import io.github.jsy.client.render.*;
import io.github.jsy.network.JRStationSignNetworking;
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

public class FabricClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Constants.LOG.info("Initializing JR Style Mod client for Fabric");

        JRStationSignNetworking.registerClient();

        // 注册方块实体渲染器
        BlockEntityRendererRegistry.register(
                FabricModBlockEntities.LINE_COLOR_BLOCK_ENTITY,
                LineColorBlockEntityRenderer::new
        );
        BlockEntityRendererRegistry.register(
                FabricModBlockEntities.JR_STATION_SIGN_BLOCK_ENTITY,
                StationSignBlockEntityRenderer::new
        );
        BlockEntityRendererRegistry.register(
                FabricModBlockEntities.PLATFORM_SIGN_BLOCK_ENTITY,
                PlatformSignBlockEntityRenderer::new
        );
        BlockEntityRendererRegistry.register(
                FabricModBlockEntities.DIRECTION_SIGN_BLOCK_ENTITY,
                DirectionSignBlockEntityRenderer::new
        );

        // 注册方块颜色提供器
        ColorProviderRegistry.BLOCK.register(new BlockColor() {
            @Override
            public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
                if (level == null || pos == null) return 0xFFFFFFFF;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof LineColorBlockEntity lineColorBE && tintIndex == 0) {
                    return lineColorBE.getLineColor();
                }
                return 0xFFFFFFFF;
            }
        }, FabricModBlocks.LINE_COLOR_BLOCK);
    }
}
