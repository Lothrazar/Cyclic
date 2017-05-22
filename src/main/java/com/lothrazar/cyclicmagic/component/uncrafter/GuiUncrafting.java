package com.lothrazar.cyclicmagic.component.uncrafter;
import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.uncrafter.TileEntityUncrafter.Fields;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
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

public class GuiUncrafting extends GuiBaseContainer {
  private TileEntityUncrafter tile;
  public GuiUncrafting(InventoryPlayer inventoryPlayer, TileEntityUncrafter tileEntity) {
    super(new ContainerUncrafting(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 3 * Const.SQ + 2 * Const.PAD + 2, TileEntityUncrafter.Fields.TIMER.ordinal(), TileEntityUncrafter.TIMER_FULL);
  }
  @Override
  public void initGui() {
    super.initGui();
    GuiButton helpBtn = new GuiButton(2,
        this.guiLeft + Const.SQ + Const.PAD + 2,
        this.guiTop + 4 * Const.PAD + 6, 12, 20, "?");
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
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerUncrafting.SLOTX_START - 1, this.guiTop + ContainerUncrafting.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    int xPrefix = 2 * Const.SQ + Const.PAD;
    int yPrefix = 2 * Const.PAD;
    for (int i = 0; i < TileEntityUncrafter.SLOT_ROWS; i++) {
      for (int j = 0; j < TileEntityUncrafter.SLOT_COLS; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ, this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
  }
}
