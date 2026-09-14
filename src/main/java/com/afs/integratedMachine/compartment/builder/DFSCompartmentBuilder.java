package com.afs.integratedMachine.compartment.builder;

import com.afs.integratedMachine.compartment.Compartment;
import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.afs.integratedMachine.compartment.blockGroup.BlockGroupType;
import com.afs.integratedMachine.compartment.blockGroup.BlockGroupTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class DFSCompartmentBuilder implements CompartmentBuilder<DFSCompartmentBuilder.DFSConfig>{
    public static final BlockGroupType CONTROLLER = BlockGroupTypes.CONTROLLER;
    public static final BlockGroupType WALL = BlockGroupTypes.WALL;
    public static final BlockGroupType CORNER = BlockGroupTypes.CORNER;
    public static final BlockGroupType INTERIOR = BlockGroupTypes.INTERIOR;

    @Override
    public Compartment build(Level level, BlockPos beginPos, DFSConfig config, String name) {
        BlockGroupList blocks = dfs(level, beginPos, config);
        if(blocks.isEmpty()){
            return Compartment.EMPTY;
        }
        if(config.includeCorner){
            setCorner(blocks);
        }
        return new Compartment(name, blocks);
    }

    private void setCorner(BlockGroupList list){
        Set<BlockPos> wall = list.getBlocks(WALL);
        Set<BlockPos> interior = list.getBlocksIfPresent(INTERIOR);
        Set<BlockPos> occupied = new HashSet<>(wall.size() + interior.size() + 1);
        occupied.addAll(wall);
        occupied.addAll(interior);

        Set<BlockPos> checked = new HashSet<>();
        BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
        for(BlockPos pos: wall){
            for(Direction dir: Direction.values()){
                if(interior.contains(m.setWithOffset(pos, dir))){
                    checked.add(m.immutable());
                }
            }
        }

        Set<BlockPos> corner = new HashSet<>();
        for(BlockPos pos: checked){
            for(int i = -1;i <= 1;i ++){
                for(int j = -1;j <= 1;j ++){
                    for(int k = -1;k <= 1;k ++){
                        m.setWithOffset(pos, i, j, k);
                        if(!occupied.contains(m)){
                            corner.add(m.immutable());
                        }
                    }
                }
            }
        }
        list.setBlocks(CORNER, corner);
    }

    private BlockGroupList dfs(Level level, BlockPos beginPos, DFSConfig config){
        BlockGroupList list = new BlockGroupList();

        Stack<BlockPos> explore = new Stack<>();
        Set<BlockPos> interior = new HashSet<>(config.maxBlocks * 4 / 3 + 1);
        Set<BlockPos> wall = new HashSet<>(config.maxBlocks * 6 + 1);

        if(isWall(level, beginPos, config) || !isValid(level, beginPos, config)){
            return BlockGroupList.EMPTY;
        }
        explore.push(beginPos);
        interior.add(beginPos);

        int n = 1;
        while(n <= config.maxBlocks){
            BlockPos current = explore.pop();
            Set<BlockPos> allPos = getSurroundedUnvisitedPos(current, interior, wall);
            n += allPos.size();
            for(BlockPos pos: allPos){
                if(!isValid(level, pos, config)){
                    return BlockGroupList.EMPTY;
                }
                else if(isWall(level, pos, config)){
                    wall.add(pos);
                }
                else{
                    interior.add(pos);
                    explore.push(pos);
                }
            }
            if(explore.isEmpty()){
                if(n <= config.maxBlocks){
                    list.setBlocks(CONTROLLER, new HashSet<>(Set.of(config.controller)));
                    list.setBlocks(INTERIOR, interior);
                    list.setBlocks(WALL, wall);
                    return list;
                }
            }
        }

        return BlockGroupList.EMPTY;
    }

    private Set<BlockPos> getSurroundedUnvisitedPos(BlockPos pos, Set<BlockPos> interior, Set<BlockPos> wall){
        Set<BlockPos> allSurround = Set.of(pos.above(), pos.below(), pos.west(), pos.east(), pos.north(), pos.south());
        return allSurround.stream().filter(p -> (!interior.contains(p)) && (!wall.contains(p)))
                .collect(Collectors.toSet());
    }

    private boolean isWall(Level level, BlockPos pos, DFSConfig config){
        BlockState state = level.getBlockState(pos);
        return state.is(config.validWall) || state.is(config.controllerType);
    }

    private boolean isValid(Level level, BlockPos pos, DFSConfig config){
        if(distanceFromController(pos, config) > config.maxDistanceFromController){
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if(state.is(config.validWall)){
            return (!state.is(config.controllerType)) || pos.equals(config.controller);
        }
        else{
            return !state.is(config.invalidInterior);
        }
    }

    private int distanceFromController(BlockPos pos, DFSConfig config){
        return Math.abs(pos.getX() - config.controller.getX()) + Math.abs(pos.getY() - config.controller.getY()) +
                Math.abs(pos.getZ() - config.controller.getZ());
    }

    public record DFSConfig(
            int maxBlocks,
            int maxDistanceFromController,
            boolean includeCorner,
            BlockPos controller,
            TagKey<Block> invalidInterior,
            TagKey<Block> validWall,
            TagKey<Block> controllerType
    ){

    }
}
