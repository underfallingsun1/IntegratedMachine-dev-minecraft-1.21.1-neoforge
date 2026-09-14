package com.afs.integratedMachine.client.gui.menu;

import com.afs.integratedMachine.block.entity.ExperienceConverterBlockEntity;
import com.afs.integratedMachine.item.IMItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class ExperienceConverterMenu extends AbstractContainerMenu {
    public static final int GEM_SLOT_X = 104;
    public static final int GEM_SLOT_Y = 18;

    public static final int BUTTON_STORE_1 = 0;
    public static final int BUTTON_STORE_10 = 1;
    public static final int BUTTON_STORE_100 = 2;
    public static final int BUTTON_STORE_ALL = 3;
    public static final int BUTTON_TAKE_1 = 4;
    public static final int BUTTON_TAKE_10 = 5;
    public static final int BUTTON_TAKE_100 = 6;
    public static final int BUTTON_TAKE_ALL = 7;
    public static final int BUTTON_PRIORITY = 8;

    public static final int[] LEVELS = {1, 10, 100, 0};

    private static final int DATA_FLUID = 0;
    private static final int DATA_CAPACITY = 1;
    private static final int DATA_PRIORITY = 2;
    private static final int DATA_GEM = 3;

    @Nullable
    private final ExperienceConverterBlockEntity blockEntity;
    private final ContainerData data;

    public ExperienceConverterMenu(int containerId, Inventory playerInventory, @Nullable ExperienceConverterBlockEntity blockEntity) {
        super(IMMenus.EXPERIENCE_CONVERTER.get(), containerId);
        this.blockEntity = blockEntity;
        if (blockEntity != null) {
            this.addSlot(new SlotItemHandler(blockEntity.getInventory(), 0, GEM_SLOT_X, GEM_SLOT_Y));
        } else {
            this.addSlot(new Slot(new SimpleContainer(1), 0, GEM_SLOT_X, GEM_SLOT_Y));
        }
        this.addPlayerInventory(playerInventory);
        this.data = createData();
        this.addDataSlots(this.data);
    }

    public ExperienceConverterMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(data.readBlockPos()) instanceof ExperienceConverterBlockEntity converter ? converter : null);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    private ContainerData createData() {
        return new ContainerData() {
            private final int[] cache = new int[4];

            @Override
            public int get(int index) {
                if (blockEntity != null && blockEntity.getLevel() != null && !blockEntity.getLevel().isClientSide) {
                    return switch (index) {
                        case DATA_FLUID -> blockEntity.getStoredFluid();
                        case DATA_CAPACITY -> blockEntity.getFluidCapacity();
                        case DATA_PRIORITY -> blockEntity.getPriority().ordinal();
                        case DATA_GEM -> blockEntity.getGemXp();
                        default -> 0;
                    };
                }
                return cache[index];
            }

            @Override
            public void set(int index, int value) {
                if (index >= 0 && index < cache.length) {
                    cache[index] = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public int getFluidAmount() {
        return this.data.get(DATA_FLUID);
    }

    public int getFluidCapacity() {
        return this.data.get(DATA_CAPACITY);
    }

    public int getPriority() {
        return this.data.get(DATA_PRIORITY);
    }

    public int getGemXp() {
        return this.data.get(DATA_GEM);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (blockEntity == null) {
            return false;
        }
        if (id == BUTTON_PRIORITY) {
            blockEntity.togglePriority();
            return true;
        }
        if (id < 0 || id > 7) {
            return false;
        }
        int levels = LEVELS[id & 3];
        if (id < 4) {
            blockEntity.storeFromPlayer(player, levels);
        } else {
            blockEntity.withdrawToPlayer(player, levels);
        }
        return true;
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) {
            return false;
        }
        return blockEntity.getLevel() != null
                && blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity
                && player.distanceToSqr(blockEntity.getBlockPos().getCenter()) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(stack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (stack.is(IMItems.XP_GEM.get())) {
                if (!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 28) {
                if (!this.moveItemStackTo(stack, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 1, 28, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return result;
    }
}
