package com.lothrazar.cyclicmagic.component.uncrafter;
import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.uncrafter.TileEntityUncrafter.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import com.lothrazar.cyclicmagic.util.UtilUncraft.UncraftResultType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUncrafting extends GuiBaseContanerProgress {
  private TileEntityUncrafter tile;
  private GuiButtonMachineRedstone redstoneBtn;
  public GuiUncrafting(InventoryPlayer inventoryPlayer, TileEntityUncrafter tileEntity) {
    super(new ContainerUncrafting(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + Const.padding,
        this.guiTop + Const.padding, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    //int buttonId, int x, int y, String buttonText)
    GuiButton helpBtn = new GuiButton(1,
        this.guiLeft + Const.SQ + Const.padding + 2,
        this.guiTop + 4 * Const.padding + 6, 12, 20, "?");
    this.buttonList.add(helpBtn);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 1) {
      ItemStack stack = this.tile.getStackInSlot(0);
      UtilUncraft.Uncrafter uncrafter = new UtilUncraft.Uncrafter();
      UncraftResultType result = uncrafter.process(stack);
      UtilChat.addChatMessage(ModCyclic.proxy.getClientPlayer(), UtilChat.lang("tile.uncrafting." + result.name().toLowerCase())
          + uncrafter.getErrorString());
    }
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    //first draw the zero slot
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUncrafting.SLOTX_START - 1, this.guiTop + ContainerUncrafting.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    int xPrefix = 2 * Const.SQ + Const.padding;
    int yPrefix = 2 * Const.padding;
    for (int i = 0; i < TileEntityUncrafter.SLOT_ROWS; i++) {
      for (int j = 0; j < TileEntityUncrafter.SLOT_COLS; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ, this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(Fields.REDSTONE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + Const.padding + 2;
  }
  public int getProgressY() {
    return this.guiTop + 3 * Const.SQ + 2 * Const.padding + 2;
  }
  public int getProgressCurrent() {
    return tile.getTimer();
  }
  public int getProgressMax() {
    return TileEntityUncrafter.TIMER_FULL;
  }
}
