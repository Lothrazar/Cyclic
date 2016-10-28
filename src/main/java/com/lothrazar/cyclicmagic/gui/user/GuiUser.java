package com.lothrazar.cyclicmagic.gui.user;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContanerProgress;
import com.lothrazar.cyclicmagic.gui.user.ContainerUser;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUser extends GuiBaseContanerProgress {
  private TileMachineUser tile;
  public GuiUser(InventoryPlayer inventoryPlayer, TileMachineUser tileEntity) {
    super(new ContainerUser(inventoryPlayer, tileEntity));
    tile = tileEntity;
  }
  public GuiUser(Container c) {
    super(c);
  }
  public String getTitle() {
    return "tile.block_user.name";
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
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);

    //yes this works, it renders the speed. Not yet used, its always 1
//    int x = ContainerMiner.SLOTX_START +40, y = 30;
//    this.fontRendererObj.drawString(tile.getSpeed()+"", x, y, 4210752);
    
//    updateDisabledButtons();
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
