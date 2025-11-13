package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.entity.custom.SlotMachineEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class SlotMachineBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<SlotMachineBlock> CODEC = createCodec(SlotMachineBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape BOTTOM_PART = Block.createCuboidShape(1,0,2,15,14,15);
    private static final VoxelShape MIDDLE_PART = Block.createCuboidShape(1,14,1,15,16,15);
    private static final VoxelShape UPPER_PART = Block.createCuboidShape(1,0,5,15,16,15);
    private static final VoxelShape LEFT_PART = Block.createCuboidShape(14,0,1,15,11,5);
    private static final VoxelShape RIGHT_PART = Block.createCuboidShape(1,0,1,2,11,5);

    private static final VoxelShape LOWER_SHAPE = VoxelShapes.union(BOTTOM_PART, MIDDLE_PART);
    private static final VoxelShape UPPER_SHAPE = VoxelShapes.union(UPPER_PART, LEFT_PART, RIGHT_PART);

    private static final Map<Direction, VoxelShape> LOWER_SHAPES_BY_DIRECTION = new EnumMap<>(Direction.class);
    private static final Map<Direction, VoxelShape> UPPER_SHAPES_BY_DIRECTION = new EnumMap<>(Direction.class);

    static {
        for (Direction dir : Direction.Type.HORIZONTAL) {
            LOWER_SHAPES_BY_DIRECTION.put(dir, rotateShape(dir, LOWER_SHAPE));
            UPPER_SHAPES_BY_DIRECTION.put(dir, rotateShape(dir, UPPER_SHAPE));
        }
    }

    public SlotMachineBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HALF, DoubleBlockHalf.LOWER));
    }

    // === CODEC ===
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    // === INTERACTION ===
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos,
                                 PlayerEntity player, BlockHitResult hit) {

        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pos.down();
            BlockState lowerState = world.getBlockState(lowerPos);
            if (lowerState.isOf(this)) {
                return this.onUse(lowerState, world, lowerPos, player, hit);
            }
            return ActionResult.PASS;
        }

        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SlotMachineEntity slotMachineEntity) {
                player.openHandledScreen(slotMachineEntity);
            }
        }

        return ActionResult.SUCCESS;
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        DoubleBlockHalf half = state.get(HALF);
        return half == DoubleBlockHalf.LOWER
                ? LOWER_SHAPES_BY_DIRECTION.get(facing)
                : UPPER_SHAPES_BY_DIRECTION.get(facing);
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
        if (!world.isClient) {
            CasinoRocket.LOGGER.info("[DEBUG] SlotMachineBlock placed at {}", pos);
            BlockEntity be = world.getBlockEntity(pos);
            CasinoRocket.LOGGER.info("[DEBUG] BlockEntity = {}", be);
        }
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
            if (half == DoubleBlockHalf.LOWER && direction == net.minecraft.util.math.Direction.UP && (!neighborState.isOf(this) || neighborState.get(HALF) != DoubleBlockHalf.UPPER)) {
                return Blocks.AIR.getDefaultState();
            }
            if (half == DoubleBlockHalf.UPPER && direction == net.minecraft.util.math.Direction.DOWN && (!neighborState.isOf(this) || neighborState.get(HALF) != DoubleBlockHalf.LOWER)) {
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

    // === BLOCK ENTITY ===
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SlotMachineEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // === HELPERS ===
    private static VoxelShape rotateShape(Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};
        int times = (to.getHorizontal() - Direction.NORTH.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1],
                    VoxelShapes.cuboid(
                            1 - maxZ, minY, minX,
                            1 - minZ, maxY, maxX
                    )));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }
        return buffer[0];
    }

}