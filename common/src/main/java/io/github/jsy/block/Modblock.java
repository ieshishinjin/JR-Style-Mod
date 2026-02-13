package io.github.jsy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 方块核心类 - 楼梯形状，带方向，精确碰撞箱，无透视
 * 完全原版API，可同时用于Fabric和Forge
 */
public class Modblock extends Block {
    // 方向属性（复用原版的FACING）
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    // ---------- 碰撞箱定义（单位：1/16像素）----------
    // 东向楼梯（默认）
    private static final VoxelShape SHAPE_EAST = Shapes.or(
            Block.box(0, 8, 0, 16, 16, 16), // 下半台阶
            Block.box(8, 0, 0, 16, 8, 16)  // 上半部分
    );

    // 南向楼梯（旋转-90度）
    private static final VoxelShape SHAPE_SOUTH = Shapes.or(
            Block.box(0, 0, 0, 16, 8, 8),
            Block.box(0, 8, 8, 16, 16, 16)
    );

    // 西向楼梯（旋转180度）
    private static final VoxelShape SHAPE_WEST = Shapes.or(
            Block.box(0, 8, 0, 16, 16, 16),
            Block.box(0, 0, 0, 8, 8, 16)
    );

    // 北向楼梯（旋转90度）
    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(0, 0, 8, 16, 8, 16),
            Block.box(0, 8, 0, 16, 16, 8)
    );

    public Modblock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(3.0f, 6.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)
                .noOcclusion()  // 🟢【解决透视】标记为非不透明，防止背面剔除
        );

        // 注册默认状态：朝北
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    // 🟢【解决透视】1.20.4 原版正确方法
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return false;  // false = 不传播天空光 = 需要渲染背后
    }

    // 🟢【解决透视】1.20.4 原版必须配套重写
    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;  // 0 = 完全透光，不产生阴影
    }

    // 🟢【解决碰撞】根据方向返回精确轮廓箱
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    // 🟢【解决碰撞】碰撞箱与轮廓箱一致
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    // 🟢【方向放置】根据玩家面向设置方块朝向
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
    }

    // 🟢【状态注册】必须重写，告诉Minecraft这个方块有哪些状态属性
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}