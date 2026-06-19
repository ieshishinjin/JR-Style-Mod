package io.github.jsy.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * 根据 MTR 线路颜色改变贴图颜色的方块
 */
public class LineColorBlock extends BaseEntityBlock {

    // 1.20.4 需要定义 codec
    public static final MapCodec<LineColorBlock> CODEC = simpleCodec(LineColorBlock::new);

    public LineColorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LineColorBlockEntity(pos, state);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        // 两侧都尝试检测：
        // - 服务端：MTRHelper 返回 null（无 MTR API），保留默认颜色
        // - 客户端：使用 MinecraftClientData 检测线路颜色，通过网络包同步回服务端
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof LineColorBlockEntity lineColorBE) {
            lineColorBE.detectLineColor(level, pos);
        }
    }
}
