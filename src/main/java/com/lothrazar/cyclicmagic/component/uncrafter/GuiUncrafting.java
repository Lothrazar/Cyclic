package com.lothrazar.cyclicmagic.component.uncrafter;
import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import com.lothrazar.cyclicmagic.util.UtilUncraft.UncraftResultType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiUncrafting extends GuiBaseContainer {
  private TileEntityUncrafter tile;
  public GuiUncrafting(InventoryPlayer inventoryPlayer, TileEntityUncrafter tileEntity) {
    super(new ContainerUncrafting(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityUncrafter.Fields.REDSTONE.ordinal();
    this.setFieldFuel(TileEntityUncrafter.Fields.FUEL.ordinal());
    this.progressBar = new ProgressBar(this, 10,
        ContainerUncrafting.SLOTY_INPUT + 2 * Const.SQ + Const.PAD,
        TileEntityUncrafter.Fields.TIMER.ordinal(), TileEntityUncrafter.TIMER_FULL);
  }
  @Override
  public void initGui() {
    super.initGui();
    GuiButton helpBtn = new GuiButton(2,
        this.guiLeft + Const.SQ + Const.PAD + 2,
        this.guiTop + ContainerUncrafting.SLOTY_INPUT - 2, 12, 20, "?");
    this.buttonList.add(helpBtn);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 2) {
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
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUncrafting.SLOTX_INPUT - 1, this.guiTop + ContainerUncrafting.SLOTY_INPUT - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    int xPrefix = 2 * Const.SQ + Const.PAD;
    for (int i = 0; i < TileEntityUncrafter.SLOT_ROWS; i++) {
      for (int j = 0; j < TileEntityUncrafter.SLOT_COLS; j++) {
        Gui.drawModalRectWithCustomSizedTexture(
            this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + ContainerUncrafting.SLOTY_INPUT + (i - 1) * Const.SQ - 1,
            u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_COAL);
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBaseMachine.SLOTX_FUEL - 1, this.guiTop + ContainerBaseMachine.SLOTY_FUEL - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }
}
