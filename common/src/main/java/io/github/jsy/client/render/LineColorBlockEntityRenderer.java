package io.github.jsy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jsy.block.LineColorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * 线路颜色方块实体渲染器 — 在方块中心显示颜色
 */
public class LineColorBlockEntityRenderer implements BlockEntityRenderer<LineColorBlockEntity> {

    public LineColorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(LineColorBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        int color = blockEntity.getLineColor();
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        float scale = 0.4f; // 更大的立方体
        poseStack.scale(scale, scale, scale);

        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        renderCube(builder, matrix, normal, r, g, b, a, packedLight);
        poseStack.popPose();
    }

    private void renderCube(VertexConsumer builder, Matrix4f matrix, Matrix3f normal,
                            float r, float g, float b, float a, int light) {
        // 前面
        quad(builder, matrix, normal, -1, -1, 1, 1, -1, 1, 1, 1, 1, -1, 1, 1, 0, 0, 1, r, g, b, a, light);
        // 后面
        quad(builder, matrix, normal, 1, -1, -1, -1, -1, -1, -1, 1, -1, 1, 1, -1, 0, 0, -1, r, g, b, a, light);
        // 右
        quad(builder, matrix, normal, 1, -1, 1, 1, -1, -1, 1, 1, -1, 1, 1, 1, 1, 0, 0, r, g, b, a, light);
        // 左
        quad(builder, matrix, normal, -1, -1, -1, -1, -1, 1, -1, 1, 1, -1, 1, -1, -1, 0, 0, r, g, b, a, light);
        // 上
        quad(builder, matrix, normal, -1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1, 0, 1, 0, r, g, b, a, light);
        // 下
        quad(builder, matrix, normal, -1, -1, -1, 1, -1, -1, 1, -1, 1, -1, -1, 1, 0, -1, 0, r, g, b, a, light);
    }

    private void quad(VertexConsumer builder, Matrix4f m, Matrix3f n,
                      float x1, float y1, float z1, float x2, float y2, float z2,
                      float x3, float y3, float z3, float x4, float y4, float z4,
                      float nx, float ny, float nz,
                      float r, float g, float b, float a, int light) {
        builder.vertex(m, x1, y1, z1).color(r, g, b, a).uv(0, 0).overlayCoords(0).uv2(light).normal(n, nx, ny, nz).endVertex();
        builder.vertex(m, x2, y2, z2).color(r, g, b, a).uv(1, 0).overlayCoords(0).uv2(light).normal(n, nx, ny, nz).endVertex();
        builder.vertex(m, x3, y3, z3).color(r, g, b, a).uv(1, 1).overlayCoords(0).uv2(light).normal(n, nx, ny, nz).endVertex();
        builder.vertex(m, x4, y4, z4).color(r, g, b, a).uv(0, 1).overlayCoords(0).uv2(light).normal(n, nx, ny, nz).endVertex();
    }

    @Override
    public int getViewDistance() {
        return 64;
    }
}
