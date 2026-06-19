package io.github.jsy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;

/**
 * 站牌文字渲染工具类
 */
public class SignTextRenderer {

    /**
     * 在站牌正面渲染居中的文字（悬挂式 / 立柱式通用）
     *
     * @param poseStack   渲染矩阵
     * @param buffer      渲染缓冲区
     * @param text        要绘制的文字
     * @param facing      方块朝向
     * @param zOffset     从方块中心到站牌表面的Z偏移（悬挂约0.44，立柱约0.31）
     * @param yOffset     Y轴偏移（正数向上）
     * @param color       文字颜色 ARGB
     * @param scale       文字缩放
     * @param packedLight 光照
     */
    public static void renderCenteredText(PoseStack poseStack, MultiBufferSource buffer,
                                          String text, Direction facing,
                                          float zOffset, float yOffset, int color,
                                          float scale, int packedLight) {
        if (text == null || text.isEmpty()) return;

        Font font = Minecraft.getInstance().font;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        // 旋转使文字朝向站牌正面
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-facing.toYRot()));
        // 移动到站牌表面
        poseStack.translate(0, yOffset, zOffset);

        float s = 0.007f * scale;
        poseStack.scale(s, -s, s);

        float textWidth = font.width(text);
        // 使用 SEE_THROUGH 确保文字在方块模型上层渲染
        font.drawInBatch(text, -textWidth / 2, 0, color, false,
                poseStack.last().pose(), buffer, Font.DisplayMode.SEE_THROUGH,
                0, packedLight);

        poseStack.popPose();
    }

    /**
     * 在线路颜色方块表面显示当前颜色（用于调试 + 可视化）
     */
    public static void renderColorOverlay(PoseStack poseStack, MultiBufferSource buffer,
                                          int argbColor, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        float r = ((argbColor >> 16) & 0xFF) / 255.0f;
        float g = ((argbColor >> 8) & 0xFF) / 255.0f;
        float b = (argbColor & 0xFF) / 255.0f;

        // 在方块中心渲染一个小彩色立方体
        VertexConsumer builder = buffer.getBuffer(RenderType.cutout());
        var matrix = poseStack.last().pose();
        var normal = poseStack.last().normal();
        float h = 0.3f;

        // 6个面
        renderQuad(builder, matrix, normal, -h, -h, h, h, -h, h, h, h, h, -h, h, h, 0, 0, 1, r, g, b, packedLight);
        renderQuad(builder, matrix, normal, h, -h, -h, -h, -h, -h, -h, h, -h, h, h, -h, 0, 0, -1, r, g, b, packedLight);
        renderQuad(builder, matrix, normal, h, -h, h, h, -h, -h, h, h, -h, h, h, h, 1, 0, 0, r, g, b, packedLight);
        renderQuad(builder, matrix, normal, -h, -h, -h, -h, -h, h, -h, h, h, -h, h, -h, -1, 0, 0, r, g, b, packedLight);
        renderQuad(builder, matrix, normal, -h, h, h, h, h, h, h, h, -h, -h, h, -h, 0, 1, 0, r, g, b, packedLight);
        renderQuad(builder, matrix, normal, -h, -h, -h, h, -h, -h, h, -h, h, -h, -h, h, 0, -1, 0, r, g, b, packedLight);

        poseStack.popPose();
    }

    private static void renderQuad(VertexConsumer builder, org.joml.Matrix4f matrix, org.joml.Matrix3f normal,
                                   float x1, float y1, float z1, float x2, float y2, float z2,
                                   float x3, float y3, float z3, float x4, float y4, float z4,
                                   float nx, float ny, float nz,
                                   float r, float g, float b, int packedLight) {
        builder.vertex(matrix, x1, y1, z1).color(r, g, b, 1.0f).uv(0, 0).overlayCoords(0).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        builder.vertex(matrix, x2, y2, z2).color(r, g, b, 1.0f).uv(1, 0).overlayCoords(0).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        builder.vertex(matrix, x3, y3, z3).color(r, g, b, 1.0f).uv(1, 1).overlayCoords(0).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
        builder.vertex(matrix, x4, y4, z4).color(r, g, b, 1.0f).uv(0, 1).overlayCoords(0).uv2(packedLight).normal(normal, nx, ny, nz).endVertex();
    }
}
