package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.games.gachapon.GachaMachinesUtils;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class GachaMachineBlock extends Block {

    public static final MapCodec<GachaMachineBlock> CODEC = createCodec(GachaMachineBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape MIDDLE_PART = Block.createCuboidShape(4,0,1,12,16,15);
    private static final VoxelShape LEFT_MIDDLE_PART = Block.createCuboidShape(12,0,2,14,16,14);
    private static final VoxelShape LEFT_PART = Block.createCuboidShape(14,0,4,15,16,12);
    private static final VoxelShape RIGHT_MIDDLE_PART = Block.createCuboidShape(2,0,2,4,16,14);
    private static final VoxelShape RIGHT_PART = Block.createCuboidShape(1,0,4,2,16,12);

    private static final VoxelShape UP_MIDDLE_PART = Block.createCuboidShape(0.5,0,0.5,15.5,1,15.5);
    private static final VoxelShape UP_LOWER_PART = Block.createCuboidShape(0,1,0,16,15,16);
    private static final VoxelShape UP_UPPER_PART = Block.createCuboidShape(0.5,15,0.5,15.5,16,15.5);

    private static final VoxelShape LOWER_SHAPE = VoxelShapes.union(MIDDLE_PART, LEFT_MIDDLE_PART, LEFT_PART, RIGHT_MIDDLE_PART, RIGHT_PART);
    private static final VoxelShape UPPER_SHAPE = VoxelShapes.union(UP_UPPER_PART, UP_MIDDLE_PART, UP_LOWER_PART);

    public GachaMachineBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    // === INTERACTION ===
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, BlockHitResult hit) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pos.down();
            BlockState lowerState = world.getBlockState(lowerPos);

            if (lowerState.isOf(this)) {
                return this.onUse(lowerState, world, lowerPos, player, hit);
            }

            return ActionResult.PASS;
        }

        if (world.isClient) return ActionResult.SUCCESS;

        ItemStack stack = player.getMainHandStack();
        String coinKey = GachaMachinesUtils.getCoinKey(stack);
        if (coinKey == null) return ActionResult.PASS;

        return GachaMachinesUtils.handleUse(world, pos, player, coinKey);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Direction facing = state.get(FACING);
        GachaMachinesUtils.finishDispense(world, pos, facing);
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPE : UPPER_SHAPE;
    }

    // === PLACEMENT ===
    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (pos.getY() < world.getTopY() - 1 && world.getBlockState(pos.up()).canReplace(ctx)) {
            return this.getDefaultState()
                    .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
    }

    // === BREAK ===
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf half = state.get(HALF);
        BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherState = world.getBlockState(otherPos);

        if (otherState.isOf(this) && otherState.get(HALF) != half) {
            world.breakBlock(otherPos, !player.isCreative());
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                   WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.get(HALF);
        if (direction.getAxis() == net.minecraft.util.math.Direction.Axis.Y) {
            if (half == DoubleBlockHalf.LOWER && direction == net.minecraft.util.math.Direction.UP && (!neighborState.isOf(this)
                    || neighborState.get(HALF) != DoubleBlockHalf.UPPER)) {
                return Blocks.AIR.getDefaultState();
            }
            if (half == DoubleBlockHalf.UPPER && direction == net.minecraft.util.math.Direction.DOWN && (!neighborState.isOf(this)
                    || neighborState.get(HALF) != DoubleBlockHalf.LOWER)) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    // === FACING ===
    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    // === PROPERTIES ===
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

}