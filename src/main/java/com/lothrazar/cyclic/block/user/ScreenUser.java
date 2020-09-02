package com.lothrazar.cyclic.block.user;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachineRedstone;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.TextboxInteger;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenUser extends ScreenBase<ContainerUser> {

  private EnergyBar energy;
  private TextboxInteger txtBox;
  private ButtonMachineRedstone btnRedstone;

  public ScreenUser(ContainerUser screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileUser.MAX);
  }

  @Override
  public void init() {
    super.init();
    energy.guiLeft = guiLeft;
    energy.guiTop = guiTop;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineRedstone(x, y, TileUser.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    //
    x = guiLeft + 120;
    y = guiTop + 28;
    txtBox = new TextboxInteger(this.font, x, y, 30,
        container.tile.getPos(), TileUser.Fields.TIMERDEL.ordinal());
    txtBox.setMaxStringLength(3);
    txtBox.setText("" + container.tile.getField(TileUser.Fields.TIMERDEL.ordinal()));
    txtBox.setTooltip(UtilChat.lang("block.cyclic.user.delay"));
    this.children.add(txtBox);
  }
  //  @Override
  //  public void removed() {  
  //    this.txtBox = null;
  //  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);//renderHoveredToolTip
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    btnRedstone.onValueUpdate(container.tile);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.drawSlot(ms, xSize / 2 - 9, 28);
    this.txtBox.render(ms, mouseX, mouseX, partialTicks);
    energy.draw(ms, container.tile.getEnergy());
  }
}
