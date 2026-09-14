package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.compartment.blockGroup.BlockGroupList;
import com.afs.integratedMachine.utils.Utils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class CompartmentPropertyReference implements CompartmentProperty{
    private final ResourceLocation key;
    private CompartmentProperty cache = null;

    public static final MapCodec<CompartmentPropertyReference> CODEC =
            ResourceLocation.CODEC.xmap(CompartmentPropertyReference::new, CompartmentPropertyReference::getKey).fieldOf("key");

    public CompartmentPropertyReference(ResourceLocation id){
        this.key = id;
    }

    public ResourceLocation getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj instanceof CompartmentPropertyReference pr){
            return key.equals(pr.key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public CompartmentProperty getProperty(Level level){
        if(cache == null) {
            RegistryAccess access = Utils.getRegistry(level);
            Registry<CompartmentProperty> properties = access.registryOrThrow(CompartmentProperty.COMPARTMENT_PROPERTY_KEY);
            cache = properties.get(key);
            if(cache == null){
                throw new IllegalStateException("unregistered property: " + key);
            }
        }
        return cache;
    }

    @Override
    public MapCodec<? extends CompartmentProperty> codec() {
        return CODEC;
    }

    @Override
    public int getPropertyValue(Level level, BlockGroupList blocks) {
        return getProperty(level).getPropertyValue(level, blocks);
    }
}
