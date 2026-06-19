package io.github.jsy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jsy.block.BlockEntityJRStationSign;
import io.github.jsy.block.BlockJRStationSign;
import io.github.jsy.block.JRStationSignVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class StationSignBlockEntityRenderer implements BlockEntityRenderer<BlockEntityJRStationSign> {

    public StationSignBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(BlockEntityJRStationSign sign, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = sign.getBlockState();
        if (!(state.getBlock() instanceof BlockJRStationSign)) return;

        Direction facing = state.getValue(BlockJRStationSign.FACING);
        boolean isPole = state.getValue(BlockJRStationSign.VARIANT) == JRStationSignVariant.POLE;

        float zOff = isPole ? 0.31f : 0.44f;   // 到站牌表面的Z距离
        float baseScale = isPole ? 0.9f : 1.0f;

        // 1. 站名（大字居中）
        String stationName = sign.getStationName();
        if (!stationName.isEmpty()) {
            SignTextRenderer.renderCenteredText(poseStack, bufferSource, stationName,
                    facing, zOff, 0.08f, 0xFF222222, 1.3f * baseScale, packedLight);
        }

        // 2. 线路名（小字在下）
        String lineName = sign.getLineName();
        if (!lineName.isEmpty()) {
            SignTextRenderer.renderCenteredText(poseStack, bufferSource, lineName,
                    facing, zOff, -0.15f, 0xFF666666, 0.55f * baseScale, packedLight);
        }

        // 3. 车站编号（右上小字）
        String stationNumber = sign.getStationNumber();
        if (!stationNumber.isEmpty()) {
            Font font = Minecraft.getInstance().font;
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-facing.toYRot()));
            poseStack.translate(0.28f, 0.22f, zOff);
            float s = 0.004f * baseScale;
            poseStack.scale(s, -s, s);
            font.drawInBatch(stationNumber, 0, 0, 0xFF444444, false,
                    poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH,
                    0, packedLight);
            poseStack.popPose();
        }

        // 4. 线路颜色条（在站名上方或下方）
        int lineColor = sign.getLineColor();
        if (lineColor != 0x333333) {
            int color = lineColor & 0xFFFFFF;
            renderColorStripe(poseStack, bufferSource, color, facing, zOff, isPole, packedLight);
        }
    }

    private void renderColorStripe(PoseStack poseStack, MultiBufferSource buffer,
                                   int color, Direction facing, float zOff,
                                   boolean isPole, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-facing.toYRot()));

        float yPos = isPole ? -0.28f : -0.22f;
        poseStack.translate(0, yPos, zOff);

        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        var builder = buffer.getBuffer(net.minecraft.client.renderer.RenderType.cutout());
        var matrix = poseStack.last().pose();
        var normal = poseStack.last().normal();
        float hw = 0.35f;
        float hh = 0.03f;

        builder.vertex(matrix, -hw, -hh, 0).color(r, g, b, 1.0f).uv(0, 1).overlayCoords(0).uv2(packedLight).normal(normal, 0, 0, 1).endVertex();
        builder.vertex(matrix, hw, -hh, 0).color(r, g, b, 1.0f).uv(1, 1).overlayCoords(0).uv2(packedLight).normal(normal, 0, 0, 1).endVertex();
        builder.vertex(matrix, hw, hh, 0).color(r, g, b, 1.0f).uv(1, 0).overlayCoords(0).uv2(packedLight).normal(normal, 0, 0, 1).endVertex();
        builder.vertex(matrix, -hw, hh, 0).color(r, g, b, 1.0f).uv(0, 0).overlayCoords(0).uv2(packedLight).normal(normal, 0, 0, 1).endVertex();

        poseStack.popPose();
    }
}
