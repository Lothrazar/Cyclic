package com.lothrazar.cyclicmagic.gui.user;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.GuiButtonMachineRedstone;
import com.lothrazar.cyclicmagic.gui.user.ContainerUser;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUser extends GuiBaseContanerProgress {
  private TileMachineUser tile;
  private GuiButtonMachineRedstone redstoneBtn;
  private ButtonUserAction actionBtn;
  public GuiUser(InventoryPlayer inventoryPlayer, TileMachineUser tileEntity) {
    super(new ContainerUser(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
  }
  public String getTitle() {
    return "tile.block_user.name";
  }
  @Override
  public void initGui() {
    super.initGui();
    redstoneBtn = new GuiButtonMachineRedstone(0,
        this.guiLeft + 8,
        this.guiTop + 8, this.tile.getPos());
    redstoneBtn.setTextureIndex(tile.getField(Fields.REDSTONE.ordinal()));
    this.buttonList.add(redstoneBtn);
    actionBtn = new ButtonUserAction(1,
        this.guiLeft + 8 + 50,
        this.guiTop + 8 + 8, this.tile.getPos());
    this.buttonList.add(actionBtn);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUser.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerUser.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    redstoneBtn.setState(tile.getField(Fields.REDSTONE.ordinal()));
    actionBtn.displayString = UtilChat.lang("tile.block_user.action" + tile.getField(Fields.LEFTRIGHT.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
  public int getProgressX() {
    return this.guiLeft + 10;
  }
  public int getProgressY() {
    return this.guiTop + 9 + 3 * Const.SQ + 5;
  }
  public int getProgressCurrent() {
    return tile.getField(Fields.TIMER.ordinal());
  }
  public int getProgressMax() {
    return TileMachineUser.TIMER_FULL;
  }
}
