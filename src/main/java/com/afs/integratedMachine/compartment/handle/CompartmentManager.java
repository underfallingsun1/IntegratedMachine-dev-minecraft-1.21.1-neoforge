package com.afs.integratedMachine.compartment.handle;

import com.afs.integratedMachine.compartment.Compartment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashSet;
import java.util.Set;

public class CompartmentManager implements INBTSerializable<CompoundTag> {
    private final Set<CompartmentList.Reference> compartments;

    public CompartmentManager() {
        this.compartments = new HashSet<>();
    }

    public void addCompartment(CompartmentList.Reference reference){
        compartments.add(reference);
    }

    public void removeCompartment(CompartmentList.Reference reference){
        compartments.remove(reference);
    }

    public CompartmentList.Reference referenceOf(Compartment compartment){
        if(compartment == Compartment.EMPTY){
            return CompartmentList.Reference.EMPTY;
        }
        for(CompartmentList.Reference ref: compartments){
            if(ref.get() == compartment){
                return ref;
            }
        }
        return CompartmentList.Reference.EMPTY;
    }

    public CompartmentList.Reference referenceOf(BlockPos controller){
        for(CompartmentList.Reference ref: compartments){
            Compartment compartment = ref.get();
            if(compartment == Compartment.EMPTY){
                continue;
            }
            if(compartment.getController().equals(controller)){
                return ref;
            }
        }
        return CompartmentList.Reference.EMPTY;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }
}
