package com.afs.integratedMachine.block;

import com.afs.integratedMachine.block.entity.ExperienceConverterBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ExperienceConverterBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape[] SHAPES = new VoxelShape[Direction.values().length];

    static {
        SHAPES[Direction.UP.ordinal()] = Shapes.or(Block.box(1, 0, 1, 15, 4, 15), Block.box(4, 4, 4, 12, 14, 12));
        SHAPES[Direction.DOWN.ordinal()] = Shapes.or(Block.box(1, 12, 1, 15, 16, 15), Block.box(4, 2, 4, 12, 12, 12));
        SHAPES[Direction.NORTH.ordinal()] = Shapes.or(Block.box(1, 1, 12, 15, 15, 16), Block.box(4, 4, 2, 12, 12, 12));
        SHAPES[Direction.SOUTH.ordinal()] = Shapes.or(Block.box(1, 1, 0, 15, 15, 4), Block.box(4, 4, 4, 12, 12, 14));
        SHAPES[Direction.EAST.ordinal()] = Shapes.or(Block.box(0, 1, 1, 4, 15, 15), Block.box(4, 4, 4, 14, 12, 12));
        SHAPES[Direction.WEST.ordinal()] = Shapes.or(Block.box(12, 1, 1, 16, 15, 15), Block.box(2, 4, 4, 12, 12, 12));
    }

    public ExperienceConverterBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(FACING).ordinal()];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(FACING).ordinal()];
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return IMBlocks.EXPERIENCE_CONVERTER_BLOCK_TYPE.get();
    }

    public static final MapCodec<ExperienceConverterBlock> CODEC = simpleCodec(ExperienceConverterBlock::new);

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExperienceConverterBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }
        return (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof ExperienceConverterBlockEntity converter) {
                converter.serverTick();
            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ExperienceConverterBlockEntity converter) {
            player.openMenu(converter, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ExperienceConverterBlockEntity converter) {
            ItemStack gem = converter.getInventory().getStackInSlot(0);
            if (!gem.isEmpty()) {
                popResource(level, pos, gem);
                converter.getInventory().setStackInSlot(0, ItemStack.EMPTY);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public static Direction getPortDirection(BlockState state) {
        return state.getValue(FACING).getOpposite();
    }
}
