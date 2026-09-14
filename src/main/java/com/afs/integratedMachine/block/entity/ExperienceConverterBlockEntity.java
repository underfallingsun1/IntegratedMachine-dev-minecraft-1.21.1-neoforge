package com.afs.integratedMachine.block.entity;

import com.afs.integratedMachine.block.ExperienceConverterBlock;
import com.afs.integratedMachine.client.gui.menu.ExperienceConverterMenu;
import com.afs.integratedMachine.fluid.IMFluids;
import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.item.XpGemItem;
import com.afs.integratedMachine.item.dataComponents.ClampedValue;
import com.afs.integratedMachine.item.dataComponents.IMDataComponents;
import com.afs.integratedMachine.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class ExperienceConverterBlockEntity extends BlockEntity implements MenuProvider {
    public static ModConfigSpec.BooleanValue ENABLE_FLUID_CACHE;
    public static ModConfigSpec.IntValue BUFFER_CAPACITY;

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(IMItems.XP_GEM.get());
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final FluidTank tank = new FluidTank(getTankCapacity(), stack -> stack.is(IMFluids.LIQUID_EXPERIENCE_SOURCE.get())) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private CachePriority priority = CachePriority.FLUID_FIRST;
    private int containerFluidAmount;
    private int containerFluidCapacity;

    public ExperienceConverterBlockEntity(BlockPos pos, BlockState blockState) {
        super(IMBlockEntityTypes.EXPERIENCE_CONVERTER.get(), pos, blockState);
    }

    public static boolean isFluidCacheEnabled() {
        return ENABLE_FLUID_CACHE != null && ENABLE_FLUID_CACHE.get();
    }

    public static int getBufferCapacity() {
        return BUFFER_CAPACITY == null ? 100000 : BUFFER_CAPACITY.get();
    }

    public static int getTankCapacity() {
        return isFluidCacheEnabled() ? getBufferCapacity() : 0;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getTank() {
        return tank;
    }

    @Nullable
    public IFluidHandler getFluidCapability() {
        return isFluidCacheEnabled() ? tank : null;
    }

    public CachePriority getPriority() {
        return priority;
    }

    public void togglePriority() {
        priority = priority.next();
        setChanged();
    }

    public int getStoredFluid() {
        return (isFluidCacheEnabled() ? tank.getFluidAmount() : 0) + containerFluidAmount;
    }

    public int getFluidCapacity() {
        return (isFluidCacheEnabled() ? tank.getCapacity() : 0) + containerFluidCapacity;
    }

    public int getGemXp() {
        ItemStack gem = inventory.getStackInSlot(0);
        if (!gem.is(IMItems.XP_GEM.get())) {
            return 0;
        }
        ClampedValue value = gem.get(IMDataComponents.XP_GEM_CAPACITY);
        return value == null ? 0 : value.value();
    }

    public void serverTick() {
        if (level == null || level.isClientSide) {
            return;
        }
        pushToContainer();
        refreshContainerCache();
    }

    @Nullable
    private IFluidHandler getContainerHandler() {
        Level level = getLevel();
        if (level == null) {
            return null;
        }
        Direction port = ExperienceConverterBlock.getPortDirection(getBlockState());
        BlockPos target = worldPosition.relative(port);
        return level.getCapability(Capabilities.FluidHandler.BLOCK, target, port.getOpposite());
    }

    private void pushToContainer() {
        if (tank.isEmpty()) {
            return;
        }
        IFluidHandler handler = getContainerHandler();
        if (handler == null) {
            return;
        }
        int accepted = handler.fill(tank.getFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
        if (accepted > 0) {
            tank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }

    private void refreshContainerCache() {
        IFluidHandler handler = getContainerHandler();
        if (handler == null) {
            containerFluidAmount = 0;
            containerFluidCapacity = 0;
            return;
        }
        int amount = 0;
        int capacity = 0;
        for (int i = 0; i < handler.getTanks(); i++) {
            capacity += handler.getTankCapacity(i);
            FluidStack stack = handler.getFluidInTank(i);
            if (stack.getFluid() == IMFluids.LIQUID_EXPERIENCE_SOURCE.get()) {
                amount += stack.getAmount();
            }
        }
        containerFluidAmount = amount;
        containerFluidCapacity = capacity;
    }

    private int fillFluid(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int filled = 0;
        IFluidHandler handler = getContainerHandler();
        if (handler != null) {
            filled += handler.fill(new FluidStack(IMFluids.LIQUID_EXPERIENCE_SOURCE.get(), amount), IFluidHandler.FluidAction.EXECUTE);
        }
        if (isFluidCacheEnabled() && filled < amount) {
            filled += tank.fill(new FluidStack(IMFluids.LIQUID_EXPERIENCE_SOURCE.get(), amount - filled), IFluidHandler.FluidAction.EXECUTE);
        }
        if (filled > 0) {
            setChanged();
        }
        return filled;
    }

    private int drainFluid(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int drained = isFluidCacheEnabled() ? tank.drain(amount, IFluidHandler.FluidAction.EXECUTE).getAmount() : 0;
        if (drained < amount) {
            IFluidHandler handler = getContainerHandler();
            if (handler != null) {
                drained += handler.drain(new FluidStack(IMFluids.LIQUID_EXPERIENCE_SOURCE.get(), amount - drained), IFluidHandler.FluidAction.EXECUTE).getAmount();
            }
        }
        if (drained > 0) {
            setChanged();
        }
        return drained;
    }

    private int addToGem(int amount) {
        ItemStack gem = inventory.getStackInSlot(0);
        if (!gem.is(IMItems.XP_GEM.get()) || amount <= 0) {
            return 0;
        }
        ClampedValue value = gem.get(IMDataComponents.XP_GEM_CAPACITY);
        if (value == null) {
            value = new ClampedValue(0, XpGemItem.XP_GEM_CAPACITY.get(), 0);
        }
        int added = Math.min(value.max() - value.value(), amount);
        if (added > 0) {
            gem.set(IMDataComponents.XP_GEM_CAPACITY, value.add(added));
            inventory.setStackInSlot(0, gem);
            setChanged();
        }
        return added;
    }

    private int removeFromGem(int amount) {
        ItemStack gem = inventory.getStackInSlot(0);
        if (!gem.is(IMItems.XP_GEM.get()) || amount <= 0) {
            return 0;
        }
        ClampedValue value = gem.get(IMDataComponents.XP_GEM_CAPACITY);
        if (value == null) {
            return 0;
        }
        int removed = Math.min(value.value(), amount);
        if (removed > 0) {
            gem.set(IMDataComponents.XP_GEM_CAPACITY, value.add(-removed));
            inventory.setStackInSlot(0, gem);
            setChanged();
        }
        return removed;
    }

    private int deposit(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int remaining = amount;
        if (priority == CachePriority.GEM_FIRST) {
            remaining -= addToGem(remaining);
            remaining -= fillFluid(remaining);
        } else {
            remaining -= fillFluid(remaining);
            remaining -= addToGem(remaining);
        }
        return amount - remaining;
    }

    private int withdraw(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int remaining = amount;
        if (priority == CachePriority.FLUID_FIRST) {
            remaining -= removeFromGem(remaining);
            remaining -= drainFluid(remaining);
        } else {
            remaining -= drainFluid(remaining);
            remaining -= removeFromGem(remaining);
        }
        return amount - remaining;
    }

    public void storeFromPlayer(Player player, int levels) {
        int currentLevel = player.experienceLevel;
        int wanted = levels <= 0 || levels >= currentLevel
                ? player.totalExperience
                : player.totalExperience - Utils.getTotalXpInLevel(currentLevel - levels);
        wanted = Math.min(wanted, player.totalExperience);
        if (wanted <= 0) {
            return;
        }
        int accepted = deposit(wanted);
        if (accepted > 0) {
            Utils.setPlayerXp(player, player.totalExperience - accepted);
            setChanged();
        }
    }

    public void withdrawToPlayer(Player player, int levels) {
        int wanted = levels <= 0
                ? getStoredFluid() + getGemXp()
                : Utils.getTotalXpInLevel(player.experienceLevel + levels) - player.totalExperience;
        if (wanted <= 0) {
            return;
        }
        int extracted = withdraw(wanted);
        if (extracted > 0) {
            Utils.setPlayerXp(player, player.totalExperience + extracted);
            setChanged();
        }
    }

    @Override
    public Component getDisplayName() {
        return getBlockState().getBlock().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ExperienceConverterMenu(containerId, inventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        if (isFluidCacheEnabled()) {
            tank.readFromNBT(registries, tag);
        } else {
            tank.setFluid(FluidStack.EMPTY);
        }
        priority = CachePriority.byName(tag.getString("Priority"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
        tank.writeToNBT(registries, tag);
        tag.putString("Priority", priority.name());
    }

    public enum CachePriority {
        FLUID_FIRST, GEM_FIRST;

        public CachePriority next() {
            return this == FLUID_FIRST ? GEM_FIRST : FLUID_FIRST;
        }

        public static CachePriority byName(String name) {
            return GEM_FIRST.name().equals(name) ? GEM_FIRST : FLUID_FIRST;
        }
    }
}
