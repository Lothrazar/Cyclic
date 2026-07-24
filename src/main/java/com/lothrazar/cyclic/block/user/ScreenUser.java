package com.lothrazar.cyclic.block.user;

import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.GuiSliderInteger;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.util.ChatUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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
    int h = 20, w = 16;
    x = leftPos + 6;
    y = topPos + 6;
    int f = TileUser.Fields.REDSTONE.ordinal();
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, f, menu.tile.getBlockPos()));
    x = leftPos + 2 * w;
    y = topPos + h + 6;
    w=120;
    f = TileUser.Fields.TIMERDEL.ordinal();
    GuiSliderInteger slider = this.addRenderableWidget(new GuiSliderInteger(x, y, w, h, f, menu.tile.getBlockPos(), 1, 64, menu.tile.getField(f)));
    slider.setTooltip(Tooltip.create(ChatUtil.ilang("block.cyclic.user.delay")));
    h = 14;
    w = 14;
    x = btnRedstone.getX() + 4;
    y = btnRedstone.getY() + h + w;
    f = TileUser.Fields.INTERACTTYPE.ordinal();
    btnType = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.SQUARE_ENDER, TextureEnum.SQUARE_RED, null));
    btnType.setWidth(w);
    btnType.setHeight(h);
    y = btnType.getY() + h + 4;
    f = TileUser.Fields.ENTITIES.ordinal();
    btnEntities = addRenderableWidget(new ButtonMachineField(x, y, f,
        menu.tile.getBlockPos(), TextureEnum.CRAFT_EMPTY, TextureEnum.CRAFT_MATCH, null));
    btnEntities.setWidth(w);
    btnEntities.setHeight(h);
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, menu.tile.getEnergy());
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(menu.tile);
    btnType.onValueUpdate(menu.tile);
    btnType.setTooltip(Tooltip.create(ChatUtil.ilang("block.cyclic.user.type." + menu.tile.doHitBreak)));
    btnEntities.onValueUpdate(menu.tile);
    btnEntities.setTooltip(Tooltip.create(ChatUtil.ilang("block.cyclic.user.entities." + menu.tile.entities)));
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, 150, 52);
    energy.draw(ms, menu.tile.getEnergy());
  }
}
