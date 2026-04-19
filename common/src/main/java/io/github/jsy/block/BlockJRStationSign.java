package io.github.jsy.block;

import com.mojang.serialization.MapCodec;
import io.github.jsy.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockJRStationSign extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<JRStationSignVariant> VARIANT = EnumProperty.create("variant", JRStationSignVariant.class);

    private static final VoxelShape POLE_NORTH = Shapes.or(
            Block.box(2, 7, 11, 14, 14, 15),
            Block.box(7, 0, 12, 9, 7, 14)
    );
    private static final VoxelShape POLE_SOUTH = Shapes.or(
            Block.box(2, 7, 1, 14, 14, 5),
            Block.box(7, 0, 2, 9, 7, 4)
    );
    private static final VoxelShape POLE_EAST = Shapes.or(
            Block.box(1, 7, 2, 5, 14, 14),
            Block.box(2, 0, 7, 4, 7, 9)
    );
    private static final VoxelShape POLE_WEST = Shapes.or(
            Block.box(11, 7, 2, 15, 14, 14),
            Block.box(12, 0, 7, 14, 7, 9)
    );
    private static final VoxelShape HANGING_NORTH = Shapes.or(
            Block.box(1, 4, 14, 15, 12, 16),
            Block.box(3, 12, 14, 5, 16, 16),
            Block.box(11, 12, 14, 13, 16, 16)
    );
    private static final VoxelShape HANGING_SOUTH = Shapes.or(
            Block.box(1, 4, 0, 15, 12, 2),
            Block.box(3, 12, 0, 5, 16, 2),
            Block.box(11, 12, 0, 13, 16, 2)
    );
    private static final VoxelShape HANGING_EAST = Shapes.or(
            Block.box(0, 4, 1, 2, 12, 15),
            Block.box(0, 12, 3, 2, 16, 5),
            Block.box(0, 12, 11, 2, 16, 13)
    );
    private static final VoxelShape HANGING_WEST = Shapes.or(
            Block.box(14, 4, 1, 16, 12, 15),
            Block.box(14, 12, 3, 16, 16, 5),
            Block.box(14, 12, 11, 16, 16, 13)
    );

    private final boolean defaultPole;
    private final MapCodec<BlockJRStationSign> codec;

    public BlockJRStationSign(boolean defaultPole) {
        this(defaultPole, Properties.of()
                .strength(2.0F, 6.0F)
                .lightLevel(state -> 8)
                .requiresCorrectToolForDrops());
    }

    private BlockJRStationSign(boolean defaultPole, Properties properties) {
        super(properties);
        this.defaultPole = defaultPole;
        this.codec = simpleCodec(props -> new BlockJRStationSign(defaultPole, props));
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(VARIANT, defaultPole ? JRStationSignVariant.POLE : JRStationSignVariant.HANGING));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityJRStationSign(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, VARIANT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        Direction facing = defaultPole
                ? context.getHorizontalDirection().getOpposite()
                : clickedFace.getAxis().isHorizontal() ? clickedFace : context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState state = defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                .setValue(VARIANT, defaultPole ? JRStationSignVariant.POLE : JRStationSignVariant.HANGING);
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
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
        return state.canSurvive(level, pos) ? super.updateShape(state, direction, neighborState, level, pos, neighborPos) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(VARIANT) == JRStationSignVariant.POLE) {
            return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
        }
        Direction supportDirection = state.getValue(FACING).getOpposite();
        BlockPos supportPos = pos.relative(supportDirection);
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, state.getValue(FACING));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (state.getValue(VARIANT) == JRStationSignVariant.POLE) {
            return switch (facing) {
                case SOUTH -> POLE_SOUTH;
                case EAST -> POLE_EAST;
                case WEST -> POLE_WEST;
                default -> POLE_NORTH;
            };
        }
        return switch (facing) {
            case SOUTH -> HANGING_SOUTH;
            case EAST -> HANGING_EAST;
            case WEST -> HANGING_WEST;
            default -> HANGING_NORTH;
        };
    }

    @Override
    public BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer && level.getBlockEntity(pos) instanceof BlockEntityJRStationSign) {
            Services.GUI_HELPER.openJRStationSignScreen(serverPlayer, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.getBlockEntity(pos) instanceof BlockEntityJRStationSign blockEntity) {
            blockEntity.syncVariantFromState(state);
            blockEntity.markUpdated();
        }
    }
}
