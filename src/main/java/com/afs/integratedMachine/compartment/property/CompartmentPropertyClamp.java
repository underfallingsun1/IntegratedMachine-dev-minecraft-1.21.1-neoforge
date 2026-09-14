package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public record CompartmentPropertyClamp(
        int min,
        int max,
        CompartmentProperty property) implements CompartmentProperty{
    public static final MapCodec<CompartmentPropertyClamp> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    Codec.INT.fieldOf("min").forGetter(CompartmentPropertyClamp::min),
                    Codec.INT.fieldOf("max").forGetter(CompartmentPropertyClamp::max),
                    Codec.lazyInitialized(() -> CompartmentProperty.CODEC).fieldOf("property").forGetter(CompartmentPropertyClamp::property)
            ).apply(inst, CompartmentPropertyClamp::new)
    );

    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        return Mth.clamp(property.getPropertyValue(level, blocks), min, max);
    }
}
