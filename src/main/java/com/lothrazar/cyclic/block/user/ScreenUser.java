package com.lothrazar.cyclic.block.user;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.components.Tooltip;

public class ScreenUser extends ScreenBase<ContainerUser> {

  private EnergyBar energy;
  private ButtonMachineField btnRedstone;
  private ButtonMachineField btnType;
  private ButtonMachineField btnEntities;

  public ScreenUser(ContainerUser screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    this.energy = new EnergyBar(this.font, TileUser.MAX);
    energy.visible = TileUser.POWERCONF.get() > 0;
    energy.guiLeft = leftPos;
    energy.guiTop = topPos;
    int x, y;
    int h = 20;
    x = leftPos + 6;
    y = topPos + 6;
    int f = TileUser.Fields.REDSTONE.ordinal();
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, f, menu.tile.getBlockPos()));
    x = leftPos + 32;
    y = topPos + 26;
    f = TileUser.Fields.TIMERDEL.ordinal();
    GuiSliderInteger slider = this.addRenderableWidget(new GuiSliderInteger(x, y, 120, h, f, menu.tile.getBlockPos(), 1, 64, menu.tile.getField(f)));
    slider.setTooltip(Tooltip.create(Component.translatable("block.cyclic.user.delay")));
    h = 14;
    x = btnRedstone.getX() + 4;
    y = btnRedstone.getY() + h + 16;
    f = TileUser.Fields.INTERACTTYPE.ordinal();
    btnType = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.SQUARE_ENDER, TextureEnum.SQUARE_RED, "block.cyclic.user.type"));
    y = btnType.getY() + h + 4;
    f = TileUser.Fields.ENTITIES.ordinal();
    btnEntities = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.CRAFT_EMPTY, TextureEnum.CRAFT_MATCH, "block.cyclic.user.entities"));
  }

  @Override
  public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms, mouseX, mouseY, partialTicks);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    btnType.onValueUpdate(menu.tile);
    btnType.setTooltip(Tooltip.create(Component.literal("block.cyclic.user.type." + menu.tile.doHitBreak)));
    btnEntities.onValueUpdate(menu.tile);
    btnEntities.setTooltip(Tooltip.create(Component.literal("block.cyclic.user.entities." + menu.tile.entities)));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void renderBg(GuiGraphics ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 150, 52);
    energy.draw(ms, menu.tile.getEnergy());
  }
}
