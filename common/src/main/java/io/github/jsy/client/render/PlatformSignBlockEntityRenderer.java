package io.github.jsy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jsy.block.BlockEntityPlatformSign;
import io.github.jsy.block.BlockPlatformSign;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 月台编号牌渲染器
 */
public class PlatformSignBlockEntityRenderer implements BlockEntityRenderer<BlockEntityPlatformSign> {

    public PlatformSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BlockEntityPlatformSign sign, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = sign.getBlockState();
        if (!(state.getBlock() instanceof BlockPlatformSign)) return;

        Direction facing = state.getValue(BlockPlatformSign.FACING);
        String number = sign.getPlatformNumber();
        if (number == null || number.isEmpty()) return;

        // 白色大字显示番线编号
        SignTextRenderer.renderCenteredText(poseStack, bufferSource, "番線 " + number,
                facing, 0.31f, 0.02f, 0xFFFFFFFF, 1.5f, packedLight);
    }
}
