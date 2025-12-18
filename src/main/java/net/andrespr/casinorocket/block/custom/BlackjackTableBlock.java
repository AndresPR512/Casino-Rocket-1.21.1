package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
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
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class BlackjackTableBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<BlackjackTableBlock> CODEC = createCodec(BlackjackTableBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private static final VoxelShape RIGHT_LEG = Block.createCuboidShape(2,0,4,6,13,14);
    private static final VoxelShape LEFT_LEG = Block.createCuboidShape(10,0,4,14,13,14);

    private static final VoxelShape TABLE1 = Block.createCuboidShape(4,13,1,12,15,2);
    private static final VoxelShape TABLE2 = Block.createCuboidShape(2,13,2,14,15,3);
    private static final VoxelShape TABLE3 = Block.createCuboidShape(1,13,3,15,15,4);
    private static final VoxelShape TABLE4 = Block.createCuboidShape(0,13,4,16,15,16);

    private static final VoxelShape BLOCK_SHAPE = VoxelShapes.union(RIGHT_LEG, LEFT_LEG, TABLE1, TABLE2, TABLE3, TABLE4);
    private static final Map<Direction, VoxelShape> BLOCK_SHAPE_BY_DIRECTION = new EnumMap<>(Direction.class);

    static {
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BLOCK_SHAPE_BY_DIRECTION.put(dir, rotateShape(dir, BLOCK_SHAPE));
        }
    }

    public BlackjackTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
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
        if (!world.isClient) {

            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof BlackjackTableEntity table) {

                if (table.isInUse() && !table.isUsedBy(player)) {
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.blackjack_table_occupied", true);
                    return ActionResult.CONSUME;
                }

                if (!table.tryLock(player)) {
                    return ActionResult.CONSUME;
                }

                player.openHandledScreen(table);
                return ActionResult.CONSUME;

            }
        }

        return ActionResult.SUCCESS;
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        return BLOCK_SHAPE_BY_DIRECTION.get(facing);
    }

    // === PLACEMENT ===
    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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
        builder.add(FACING);
    }

    // === BLOCK ENTITY ===
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlackjackTableEntity(pos, state);
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