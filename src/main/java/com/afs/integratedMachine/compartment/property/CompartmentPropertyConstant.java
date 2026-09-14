package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.Level;

public record CompartmentPropertyConstant(int value) implements CompartmentProperty {
    public static final MapCodec<CompartmentPropertyConstant> CODEC = Codec.INT.xmap(CompartmentPropertyConstant::new, CompartmentPropertyConstant::value).fieldOf("value");

    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        return value;
    }
}
