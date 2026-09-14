package com.afs.integratedMachine.compartment.builder;

import com.afs.integratedMachine.compartment.Compartment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface CompartmentBuilder<C> {
    Compartment build(Level level, BlockPos beginPos, C config, String name);
}
