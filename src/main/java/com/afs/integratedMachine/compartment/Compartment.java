package com.afs.integratedMachine.compartment;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.afs.integratedMachine.compartment.blockGroup.BlockGroupTypes;
import com.afs.integratedMachine.compartment.property.CompartmentProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class Compartment{
    private final String name;
    private final BlockGroupList blocks;
    private BlockPos controller = null;
    private final Map<CompartmentProperty, Integer> propertyCache = new HashMap<>();
    public Compartment(String name, BlockGroupList blocks){
        this.name = name;
        this.blocks = blocks;
    }

    public String getName() {
        return name;
    }

    public int getProperty(Level level, CompartmentProperty property){
        if(propertyCache.containsKey(property)){
            return propertyCache.get(property);
        }
        int v = property.getPropertyValue(level, blocks);
        propertyCache.put(property, v);
        return v;
    }

    public BlockPos getController(){
        if(controller == null){
            controller = blocks.getBlocks(BlockGroupTypes.CONTROLLER).iterator().next();
        }
        return controller;
    }

    public BlockGroupList getBlocks(){
        return blocks;
    }

    public CompoundTag save(HolderLookup.Provider registry){
        CompoundTag tag = new CompoundTag();
        if(this == EMPTY){
            return tag;
        }
        tag.put("blocks", blocks.save());
        tag.putString("name", name);
        return tag;
    }

    public static Compartment load(CompoundTag tag, HolderLookup.Provider registry){
        if(tag.isEmpty()){
            return EMPTY;
        }
        else{
            String name = tag.getString("name");
            BlockGroupList blocks = BlockGroupList.load(tag.getCompound("blocks"));
            return new Compartment(name, blocks);
        }
    }

    public static final Compartment EMPTY = new Compartment("empty", new BlockGroupList());
    public static final Compartment CLIENT_WAITING = new Compartment("client_waiting", new BlockGroupList());
}
