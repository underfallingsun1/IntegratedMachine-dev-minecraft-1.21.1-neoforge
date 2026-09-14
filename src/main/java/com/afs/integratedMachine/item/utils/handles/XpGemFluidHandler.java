package com.afs.integratedMachine.item.utils.handles;

import com.afs.integratedMachine.fluid.IMFluids;
import com.afs.integratedMachine.item.XpGemItem;
import com.afs.integratedMachine.item.dataComponents.ClampedValue;
import com.afs.integratedMachine.item.dataComponents.IMDataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class XpGemFluidHandler implements IFluidHandlerItem {
    private final ItemStack container;

    public XpGemFluidHandler(ItemStack container) {
        this.container = container;
    }

    private ClampedValue getValue() {
        ClampedValue value = container.get(IMDataComponents.XP_GEM_CAPACITY);
        if (value == null) {
            value = new ClampedValue(0, XpGemItem.XP_GEM_CAPACITY.get(), 0);
        }
        return value;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        int amount = getValue().value();
        return amount <= 0
                ? FluidStack.EMPTY
                : new FluidStack(IMFluids.LIQUID_EXPERIENCE_SOURCE.get(), amount);
    }

    @Override
    public int getTankCapacity(int tank) {
        return getValue().max();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return stack.is(IMFluids.LIQUID_EXPERIENCE_SOURCE.get());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(0, resource)) {
            return 0;
        }
        ClampedValue value = getValue();
        int accepted = Math.min(resource.getAmount(), value.max() - value.value());
        if (accepted <= 0) {
            return 0;
        }
        if (action.execute()) {
            container.set(IMDataComponents.XP_GEM_CAPACITY, value.add(accepted));
        }
        return accepted;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(0, resource)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        ClampedValue value = getValue();
        int drained = Math.min(maxDrain, value.value());
        if (drained <= 0) {
            return FluidStack.EMPTY;
        }
        if (action.execute()) {
            container.set(IMDataComponents.XP_GEM_CAPACITY, value.add(-drained));
        }
        return new FluidStack(IMFluids.LIQUID_EXPERIENCE_SOURCE.get(), drained);
    }

    @Override
    public ItemStack getContainer() {
        return container;
    }
}
