package io.github.jsy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jsy.block.BlockDirectionSign;
import io.github.jsy.block.BlockEntityDirectionSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 方向指示牌渲染器
 */
public class DirectionSignBlockEntityRenderer implements BlockEntityRenderer<BlockEntityDirectionSign> {

    public DirectionSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BlockEntityDirectionSign sign, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = sign.getBlockState();
        if (!(state.getBlock() instanceof BlockDirectionSign)) return;

        Direction facing = state.getValue(BlockDirectionSign.FACING);
        String text = sign.getDirectionText();
        if (text == null || text.isEmpty()) return;

        // 白色大字居中显示方向文字
        SignTextRenderer.renderCenteredText(poseStack, bufferSource, text,
                facing, 0.44f, 0.05f, 0xFFFFFFFF, 1.2f, packedLight);
    }
}
