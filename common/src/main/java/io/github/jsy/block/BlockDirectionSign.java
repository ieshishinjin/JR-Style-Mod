package io.github.jsy.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockDirectionSign extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(1, 4, 14, 15, 12, 16),
            Block.box(3, 12, 14, 5, 16, 16),
            Block.box(11, 12, 14, 13, 16, 16)
    );
    private static final VoxelShape SHAPE_SOUTH = Shapes.or(
            Block.box(1, 4, 0, 15, 12, 2),
            Block.box(3, 12, 0, 5, 16, 2),
            Block.box(11, 12, 0, 13, 16, 2)
    );
    private static final VoxelShape SHAPE_EAST = Shapes.or(
            Block.box(0, 4, 1, 2, 12, 15),
            Block.box(0, 12, 3, 2, 16, 5),
            Block.box(0, 12, 11, 2, 16, 13)
    );
    private static final VoxelShape SHAPE_WEST = Shapes.or(
            Block.box(14, 4, 1, 16, 12, 15),
            Block.box(14, 12, 3, 16, 16, 5),
            Block.box(14, 12, 11, 16, 16, 13)
    );

    public BlockDirectionSign() {
        super(Properties.of()
                .strength(2.0F, 6.0F)
                .lightLevel(state -> 8)
                .requiresCorrectToolForDrops());
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        Direction facing = clickedFace.getAxis().isHorizontal() ? clickedFace : context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING).getOpposite();
        BlockPos supportPos = pos.relative(direction);
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, state.getValue(FACING));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            io.github.jsy.platform.Services.GUI_HELPER.openDirectionSignScreen(serverPlayer, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
