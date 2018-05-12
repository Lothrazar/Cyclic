/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.core.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.ITileStackWrapper;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.FluidBar;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonTogglePreview;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleRedstone;
import com.lothrazar.cyclicmagic.net.PacketTileStackWrapped;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GuiBaseContainer extends GuiContainer {

  public final static int FONTCOLOR = 4210752;
  final static int stackWrapperColor = -1130706433;
  public TileEntityBaseMachineInvo tile;
  private Const.ScreenSize screenSize = ScreenSize.STANDARD;
  protected int fieldRedstoneBtn = -1;
  protected int fieldPreviewBtn = -1;
  protected ArrayList<GuiTextField> txtBoxes = new ArrayList<GuiTextField>();
  public ArrayList<ButtonTriggerWrapper> buttonWrappers = new ArrayList<ButtonTriggerWrapper>();
  public ProgressBar progressBar = null;
  public EnergyBar energyBar = null;
  public FluidBar fluidBar = null;
  private GuiButtonToggleRedstone redstoneBtn = null;
  private GuiButtonTogglePreview btnPreview;

  public GuiBaseContainer(Container inventorySlotsIn, TileEntityBaseMachineInvo tile) {
    super(inventorySlotsIn);
    this.tile = tile;
  }

  public GuiBaseContainer(Container inventorySlotsIn) {
    super(inventorySlotsIn);
    this.tile = null;
  }

  protected void setScreenSize(Const.ScreenSize ss) {
    this.screenSize = ss;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
  }

  protected Const.ScreenSize getScreenSize() {
    return screenSize;
  }

  @Override
  public void initGui() {
    super.initGui();
    int x = this.guiLeft + Const.PAD / 2;
    int y = this.guiTop + Const.PAD / 2;
    if (this.fieldRedstoneBtn >= 0) {
      redstoneBtn = new GuiButtonToggleRedstone(1,
          x,
          y, this.tile.getPos());
      this.buttonList.add(redstoneBtn);
      y += Const.PAD / 2 + redstoneBtn.width;
    }
    if (this.fieldPreviewBtn > 0) {
      btnPreview = new GuiButtonTogglePreview(2,
          x,
          y, this.tile.getPos());
      this.buttonList.add(btnPreview);
    }
  }

  /**
   * ONLY CALL FROM drawGuiContainerForegroundLayer
   * 
   * @param x
   * @param y
   * @param f
   */
  protected void drawFieldAt(int x, int y, int f) {
    String display = "" + this.tile.getField(f);
    x = (display.length() > 1) ? x - 3 : x;
    this.drawString(display, x, y);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    drawNameText();
    updateToggleButtonStates();
    for (GuiTextField txt : txtBoxes) {
      if (txt != null) {
        txt.drawTextBox();
      }
    }
  }

  /**
   * shift the x param over if the length is over 1, to center between the two digits made for numeric strings up to 99
   * 
   * @param display
   * @param x
   * @param y
   */
  public void drawStringCenteredCheckLength(String display, int x, int y) {
    x = (display.length() > 1) ? x - 3 : x;
    this.drawString(display, x, y);
  }

  public void drawNameText() {
    if (tile != null) {
      String s = UtilChat.lang(tile.getName());
      this.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6);
    }
  }

  public void updateToggleButtonStates() {
    if (redstoneBtn != null) {
      redstoneBtn.setState(tile.getField(this.fieldRedstoneBtn));
    }
    if (btnPreview != null) {
      if (tile.getField(this.fieldPreviewBtn) == 1) {
        btnPreview.setStateOn();
      }
      else {
        btnPreview.setStateOff();
      }
    }
    updateDisabledButtonTriggers();
  }

  public void drawStringCentered(String s, int x, int y) {
    this.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, y);
  }

  public void drawString(String s, int x, int y) {
    this.fontRenderer.drawString(UtilChat.lang(s), x, y, FONTCOLOR);
  }

  public int getMiddleY() {
    int yMiddle = (this.height - this.ySize) / 2;
    return yMiddle;
  }

  public int getMiddleX() {
    int xMiddle = (this.width - this.xSize) / 2;
    return xMiddle;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    //      super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);// abstract
    this.drawDefaultBackground();//dim the background as normal
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(getScreenSize().texture());
    int thisX = getMiddleX();
    int thisY = getMiddleY();
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v,
        getScreenSize().width(), getScreenSize().height(),
        getScreenSize().width(), getScreenSize().height());
    if (this.progressBar != null) {
      progressBar.draw();
    }
    if (this.energyBar != null && tile != null
        && tile.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
      energyBar.draw(tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP));
    }
  }

  private String getFuelAmtDisplay() {
    IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
    return energy.getEnergyStored() + "/" + energy.getMaxEnergyStored();
  }

  private void renderEnergyTooltip(int mouseX, int mouseY) {
    String display = getFuelAmtDisplay();
    drawHoveringText(Arrays.asList(display), mouseX, mouseY, fontRenderer);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderHoveredToolTip(mouseX, mouseY);
    drawStackWrappers(mouseX, mouseY);
    if (energyBar != null && energyBar.isMouseover(mouseX, mouseY)) {
      this.renderEnergyTooltip(mouseX, mouseY);
    }
    if (fluidBar != null && fluidBar.isMouseover(mouseX, mouseY)) {
      int has = ((TileEntityBaseMachineFluid) tile).getCurrentFluidStackAmount();
      drawHoveringText(Arrays.asList(has + "/" + fluidBar.getCapacity()), mouseX, mouseY, fontRenderer);
    }
    drawButtonTooltips(mouseX, mouseY);
  }

  private void drawButtonTooltips(int mouseX, int mouseY) {
    ITooltipButton btn;
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton) {
        btn = (ITooltipButton) buttonList.get(i);
        if (btn.getTooltips() != null) {
          drawHoveringText(btn.getTooltips(), mouseX, mouseY);
        }
        break;// cant hover on 2 at once
      }
    }
  }

  private void drawStackWrappers(int mouseX, int mouseY) {
    if (tile instanceof ITileStackWrapper) {
      ITileStackWrapper te = (ITileStackWrapper) tile;
      StackWrapper wrap;
      for (int i = 0; i < te.getWrapperCount(); i++) {
        wrap = te.getStackWrapper(i);
        if (isPointInRegion(wrap.getX() - guiLeft, wrap.getY() - guiTop, Const.SQ - 2, Const.SQ - 2, mouseX, mouseY)) {
          //      this.hoveredSlot = slot;
          GlStateManager.disableLighting();
          GlStateManager.disableDepth();
          int j1 = wrap.getX() + 1;
          int k1 = wrap.getY() + 1;
          GlStateManager.colorMask(true, true, true, false);
          this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, stackWrapperColor, stackWrapperColor);
          GlStateManager.colorMask(true, true, true, true);
          GlStateManager.enableLighting();
          GlStateManager.enableDepth();
          if (wrap.isEmpty() == false)
            this.renderToolTip(wrap.getStack(), mouseX, mouseY);
        }
      }
    }
  }

  @Override
  protected <T extends GuiButton> T addButton(T buttonIn) {
    return super.addButton(buttonIn);
  }

  protected void registerButtonDisableTrigger(GuiButton buttonIn, ButtonTriggerWrapper.ButtonTriggerType trigger,
      int fieldId, int fv) {
    this.buttonWrappers.add(new ButtonTriggerWrapper(buttonIn, trigger, fieldId, fv));
  }

  private void updateDisabledButtonTriggers() {
    for (ButtonTriggerWrapper btnWrap : this.buttonWrappers) {
      int fieldValue = this.tile.getField(btnWrap.fld);
      boolean isDisabled = true;
      switch (btnWrap.trig) {
        case EQUAL:
          isDisabled = (fieldValue == btnWrap.triggerValue);
        break;
        case GREATER:
          isDisabled = (fieldValue > btnWrap.triggerValue);
        break;
        case LESS:
          isDisabled = (fieldValue < btnWrap.triggerValue);
        break;
        case NOTEQUAL:
          isDisabled = (fieldValue != btnWrap.triggerValue);
        break;
      }
      btnWrap.btn.enabled = !isDisabled;
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
    super.mouseClicked(mouseX, mouseY, btn);// x/y pos is 33/30
    if (tile instanceof ITileStackWrapper) {
      mouseClickedWrapper((ITileStackWrapper) tile, mouseX, mouseY);
    }
    if (txtBoxes != null) {
      mouseClickedTextboxes(mouseX, mouseY, btn);
    }
  }

  private void mouseClickedTextboxes(int mouseX, int mouseY, int btn) {
    for (GuiTextField txt : txtBoxes) {
      txt.mouseClicked(mouseX, mouseY, btn);
      if (btn == 0) {//basically left click
        boolean flag = mouseX >= this.guiLeft + txt.x && mouseX < this.guiLeft + txt.x + txt.width
            && mouseY >= this.guiTop + txt.y && mouseY < this.guiTop + txt.y + txt.height;
        txt.setFocused(flag);
      }
    }
  }

  protected void mouseClickedWrapper(ITileStackWrapper te, int mouseX, int mouseY) {
    ItemStack stackInMouse = mc.player.inventory.getItemStack();
    StackWrapper wrap;
    for (int i = 0; i < te.getWrapperCount(); i++) {
      wrap = te.getStackWrapper(i);
      if (isPointInRegion(wrap.getX() - guiLeft, wrap.getY() - guiTop, Const.SQ - 2, Const.SQ - 2, mouseX, mouseY)) {
        if (stackInMouse.isEmpty() && wrap.isEmpty()) {
          //if both empty, do nothing. dont waste a packet
          break;
        }
        if (stackInMouse.isEmpty()) {
          wrap.setStack(ItemStack.EMPTY);
        }
        else {
          wrap.setStack(stackInMouse.copy());
          wrap.setCount(1);
        }
        //PACKET TIIIIME
        ModCyclic.network.sendToServer(new PacketTileStackWrapped(i, wrap, tile.getPos()));
        return;
      }
    }
  }

  public void renderStackWrappers(ITileStackWrapper te) {
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int i = 0; i < te.getWrapperCount(); i++) {
      //set its position for mouseclick later
      StackWrapper wrap = te.getStackWrapper(i);
      Gui.drawModalRectWithCustomSizedTexture(
          wrap.getX(), wrap.getY(),
          0, 0, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int i = 0; i < te.getWrapperCount(); i++) {
      //set its position for mouseclick later
      StackWrapper wrap = te.getStackWrapper(i);
      if (wrap.isEmpty() == false) {
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(wrap.getStack(), wrap.getX() + 1, wrap.getY() + 1);
        //keep this render quantity for later
        //          mc.getRenderItem().renderItemOverlayIntoGUI(fontRenderer, s, x + 1, y + 1, "1");
        GlStateManager.popMatrix();
      }
    }
  }
}
