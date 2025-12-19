package net.andrespr.casinorocket.block.custom;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.block.entity.custom.ChipTableEntity;
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

public class ChipTableBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<ChipTableBlock> CODEC = createCodec(ChipTableBlock::new);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private static final VoxelShape LEG1 = Block.createCuboidShape(1,0,1,3,15,3);
    private static final VoxelShape LEG2 = Block.createCuboidShape(1,0,13,3,15,15);
    private static final VoxelShape LEG3 = Block.createCuboidShape(13,0,13,15,15,15);
    private static final VoxelShape LEG4 = Block.createCuboidShape(13,0,1,15,15,3);
    private static final VoxelShape LEGS = VoxelShapes.union(LEG1, LEG2, LEG3, LEG4);

    private static final VoxelShape STAND1 = Block.createCuboidShape(3,4,1,13,6,3);
    private static final VoxelShape STAND2 = Block.createCuboidShape(1,4,3,3,6,13);
    private static final VoxelShape STAND3 = Block.createCuboidShape(3,4,13,13,6,15);
    private static final VoxelShape STAND4 = Block.createCuboidShape(13,4,3,15,6,13);
    private static final VoxelShape STAND = VoxelShapes.union(STAND1, STAND2, STAND3, STAND4);

    private static final VoxelShape TABLE1 = Block.createCuboidShape(3,11,1,13,13,3);
    private static final VoxelShape TABLE2 = Block.createCuboidShape(3,11,13,13,13,15);
    private static final VoxelShape TABLE3 = Block.createCuboidShape(1,11,3,3,13,13);
    private static final VoxelShape TABLE4 = Block.createCuboidShape(13,11,3,15,13,13);
    private static final VoxelShape TABLES = VoxelShapes.union(TABLE1, TABLE2, TABLE3, TABLE4);

    private static final VoxelShape CHIPS = Block.createCuboidShape(3,9,3,13,15,13);
    private static final VoxelShape BLOCK_SHAPE = VoxelShapes.union(LEGS, STAND, TABLES, CHIPS);

    public ChipTableBlock(Settings settings) {
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof ChipTableEntity chipTableEntity) {
            player.openHandledScreen(chipTableEntity);
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }

    // === SHAPE ===
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BLOCK_SHAPE;
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
        return new ChipTableEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}