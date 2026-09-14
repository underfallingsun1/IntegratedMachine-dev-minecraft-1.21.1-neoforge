package com.afs.integratedMachine.block;

import com.afs.integratedMachine.block.entity.CompartmentControllerBlockEntity;
import com.afs.integratedMachine.block.utils.IMBlockStateProperties;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CompartmentControllerBlock extends Block implements EntityBlock {
    private final int maxCount;
    private final int maxDistance;

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ACTIVE = IMBlockStateProperties.ACTIVE;

    public CompartmentControllerBlock(Properties properties, int maxCount, int maxDistance) {
        super(properties);
        this.maxCount = maxCount;
        this.maxDistance = maxDistance;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CompartmentControllerBlockEntity(pos, state);
    }

    public static final MapCodec<CompartmentControllerBlock> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    BlockBehaviour.propertiesCodec(),
                    Codec.INT.fieldOf("max_count").forGetter(CompartmentControllerBlock::getMaxCount),
                    Codec.INT.fieldOf("max_distance").forGetter(CompartmentControllerBlock::getMaxDistance)
            ).apply(inst, CompartmentControllerBlock::new)
    );

    public int getMaxCount() {
        return maxCount;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return IMBlocks.COMPARTMENT_CONTROLLER_BLOCK_TYPE.get();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        return defaultBlockState().setValue(FACING, face);
    }
}
