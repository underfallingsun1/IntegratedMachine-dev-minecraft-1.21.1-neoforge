package com.afs.integratedMachine.client.gui.screen;

import com.afs.integratedMachine.client.gui.menu.ExperienceConverterMenu;
import com.afs.integratedMachine.utils.LangComps;
import com.afs.integratedMachine.utils.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class ExperienceConverterScreen extends AbstractContainerScreen<ExperienceConverterMenu> {
    private static final ResourceLocation TEXTURE = Utils.modLoc("textures/gui/experience_converter.png");

    private static final int TANK_X = 150;
    private static final int TANK_Y = 18;
    private static final int TANK_W = 18;
    private static final int TANK_H = 46;
    private static final int FLUID_COLOR = 0xFF35D435;

    private Button priorityButton;

    public ExperienceConverterScreen(ExperienceConverterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < 4; i++) {
            Component label = levelLabel(ExperienceConverterMenu.LEVELS[i]);
            int storeId = ExperienceConverterMenu.BUTTON_STORE_1 + i;
            this.addRenderableWidget(Button.builder(label, b -> click(storeId))
                    .bounds(this.leftPos + 8 + i * 22, this.topPos + 18, 20, 20)
                    .tooltip(Tooltip.create(LangComps.EXPERIENCE_CONVERTER_STORE.apply(label)))
                    .build());
            int takeId = ExperienceConverterMenu.BUTTON_TAKE_1 + i;
            this.addRenderableWidget(Button.builder(label, b -> click(takeId))
                    .bounds(this.leftPos + 8 + i * 22, this.topPos + 42, 20, 20)
                    .tooltip(Tooltip.create(LangComps.EXPERIENCE_CONVERTER_TAKE.apply(label)))
                    .build());
        }
        this.priorityButton = Button.builder(priorityLabel(), b -> click(ExperienceConverterMenu.BUTTON_PRIORITY))
                .bounds(this.leftPos + 100, this.topPos + 44, 48, 20)
                .tooltip(Tooltip.create(LangComps.EXPERIENCE_CONVERTER_PRIORITY_TOOLTIP.apply()))
                .build();
        this.addRenderableWidget(this.priorityButton);
    }

    private void click(int id) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
        }
    }

    private Component levelLabel(int levels) {
        return levels <= 0 ? LangComps.EXPERIENCE_CONVERTER_ALL.apply() : Component.literal(String.valueOf(levels));
    }

    private Component priorityLabel() {
        return this.menu.getPriority() == 0
                ? LangComps.EXPERIENCE_CONVERTER_PRIORITY_FLUID.apply()
                : LangComps.EXPERIENCE_CONVERTER_PRIORITY_GEM.apply();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.priorityButton != null) {
            this.priorityButton.setMessage(priorityLabel());
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int amount = this.menu.getFluidAmount();
        int capacity = this.menu.getFluidCapacity();
        if (amount > 0 && capacity > 0) {
            int innerX = this.leftPos + TANK_X + 1;
            int innerY = this.topPos + TANK_Y + 1;
            int innerW = TANK_W - 2;
            int innerH = TANK_H - 2;
            int filled = Mth.clamp((int) ((long) innerH * amount / capacity), 0, innerH);
            if (filled > 0) {
                guiGraphics.fill(innerX, innerY + innerH - filled, innerX + innerW, innerY + innerH, FLUID_COLOR);
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        int relX = mouseX - this.leftPos;
        int relY = mouseY - this.topPos;
        if (relX >= TANK_X && relX < TANK_X + TANK_W && relY >= TANK_Y && relY < TANK_Y + TANK_H) {
            guiGraphics.renderTooltip(this.font, LangComps.EXPERIENCE_CONVERTER_FLUID.apply(this.menu.getFluidAmount(), this.menu.getFluidCapacity()), mouseX, mouseY);
        }
    }
}
