package io.github.jsy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.jsy.block.LineColorBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/**
 * 线路颜色方块实体渲染器
 * 用于在方块上渲染额外的视觉效果
 */
public class LineColorBlockEntityRenderer implements BlockEntityRenderer<LineColorBlockEntity> {

    public LineColorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(LineColorBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        // 获取线路颜色
        int color = blockEntity.getLineColor();
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        // 如果需要渲染额外的效果（如发光线条），可以在这里添加
        // 目前我们主要依靠方块颜色提供器（BlockColor）来处理贴图染色

        // 示例：在方块中心渲染一个小立方体表示线路颜色
        poseStack.pushPose();

        // 移动到方块中心
        poseStack.translate(0.5, 0.5, 0.5);

        // 缩放到一个小立方体
        float scale = 0.25f;
        poseStack.scale(scale, scale, scale);

        // 渲染彩色立方体
        VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        // 渲染立方体的六个面
        renderColoredCube(builder, matrix, normal, r, g, b, a, packedLight);

        poseStack.popPose();
    }

    private void renderColoredCube(VertexConsumer builder, Matrix4f matrix, Matrix3f normal,
                                   float r, float g, float b, float a, int packedLight) {
        // 前面 (Z+)
        renderQuad(builder, matrix, normal,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                0, 0, 1, r, g, b, a, packedLight);

        // 后面 (Z-)
        renderQuad(builder, matrix, normal,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0, 0, -1, r, g, b, a, packedLight);

        // 右面 (X+)
        renderQuad(builder, matrix, normal,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                1, 0, 0, r, g, b, a, packedLight);

        // 左面 (X-)
        renderQuad(builder, matrix, normal,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -1, 0, 0, r, g, b, a, packedLight);

        // 上面 (Y+)
        renderQuad(builder, matrix, normal,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0, 1, 0, r, g, b, a, packedLight);

        // 下面 (Y-)
        renderQuad(builder, matrix, normal,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0, -1, 0, r, g, b, a, packedLight);
    }

    private void renderQuad(VertexConsumer builder, Matrix4f matrix, Matrix3f normal,
                            float x1, float y1, float z1,
                            float x2, float y2, float z2,
                            float x3, float y3, float z3,
                            float x4, float y4, float z4,
                            float nx, float ny, float nz,
                            float r, float g, float b, float a, int packedLight) {

        // 顶点 1
        builder.vertex(matrix, x1, y1, z1)
                .color(r, g, b, a)
                .uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normal, nx, ny, nz)
                .endVertex();

        // 顶点 2
        builder.vertex(matrix, x2, y2, z2)
                .color(r, g, b, a)
                .uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normal, nx, ny, nz)
                .endVertex();

        // 顶点 3
        builder.vertex(matrix, x3, y3, z3)
                .color(r, g, b, a)
                .uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normal, nx, ny, nz)
                .endVertex();

        // 顶点 4
        builder.vertex(matrix, x4, y4, z4)
                .color(r, g, b, a)
                .uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normal, nx, ny, nz)
                .endVertex();
    }

    @Override
    public int getViewDistance() {
        return 64;
    }
}
