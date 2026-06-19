package io.github.jsy.client;

import io.github.jsy.Constants;
import io.github.jsy.block.LineColorBlockEntity;
import io.github.jsy.block.ModBlockEntities;
import io.github.jsy.block.ModBlocks;
import io.github.jsy.client.render.*;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientSetup {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Constants.LOG.info("Registering Forge block entity renderers");
        event.registerBlockEntityRenderer(
                ModBlockEntities.LINE_COLOR_BLOCK_ENTITY,
                LineColorBlockEntityRenderer::new
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.JR_STATION_SIGN_BLOCK_ENTITY,
                StationSignBlockEntityRenderer::new
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.PLATFORM_SIGN_BLOCK_ENTITY,
                PlatformSignBlockEntityRenderer::new
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.DIRECTION_SIGN_BLOCK_ENTITY,
                DirectionSignBlockEntityRenderer::new
        );
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        Constants.LOG.info("Registering Forge block color providers");
        event.register(new BlockColor() {
            @Override
            public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
                if (level == null || pos == null) return 0xFFFFFFFF;
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof LineColorBlockEntity lineColorBE && tintIndex == 0) {
                    return lineColorBE.getLineColor();
                }
                return 0xFFFFFFFF;
            }
        }, ModBlocks.LINE_COLOR_BLOCK);
    }
}
